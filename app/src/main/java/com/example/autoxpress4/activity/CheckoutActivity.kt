package com.example.autoxpress4.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.autoxpress4.MainActivity
import com.example.autoxpress4.R
import com.example.autoxpress4.roomdb.AppDatabase
import com.example.autoxpress4.roomdb.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout) 

        val checkout = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        checkout.setKeyID("rzp_live_XXXXXXXXXXXXXX")
        val price=intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name", "Pkart")
            options.put("description", "Reference No. #123456")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("order_id", "order_DBJOWzybf0sJbb") // from response of step 3
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt()*100).toString()) // pass amount in currency subunits
            options.put("prefill.email", "shivamdoase@gmail.com")
            options.put("prefill.contact", "9359819562")
            //val retryObj = JSONObject()
            //retryObj.put("enabled", true)
            //retryObj.put("max_count", 4)
            //options.put("retry", retryObj)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
        uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for(currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {

        val dao =AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {
                lifecycleScope.launch (Dispatchers.IO){
                    dao.deleteProduct(ProductModel(productId))
                }

                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId)
            }

    }

    private fun saveData(name: String?, price: String?, productId: String) {
        val preferences=this.getSharedPreferences("user",MODE_PRIVATE)
        val data= hashMapOf<String,Any>()
        data["name"]=name!!
        data["price"]=price!!
        data["productId"]=productId
        data["status"]="Ordered"
        data["userId"]=preferences.getString("number","")!!
        val firestore =Firebase.firestore.collection("allOrders")
        val key=firestore.document().id
        data["orderId"]=key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this,"Order Placed",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))


        }.addOnFailureListener{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Error",Toast.LENGTH_SHORT).show()
    }





}