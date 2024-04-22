package com.example.autoxpress4.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.autoxpress4.MainActivity
import com.example.autoxpress4.R
import com.example.autoxpress4.databinding.ActivityProductDetailBinding
import com.example.autoxpress4.roomdb.AppDatabase
import com.example.autoxpress4.roomdb.ProductDao
import com.example.autoxpress4.roomdb.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailBinding.inflate(layoutInflater)
        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)

    }

    private fun getProductDetails(prodId: String?) {

        Firebase.firestore.collection("products")
            .document(prodId!!).get().addOnSuccessListener{
                val list = it.get("productImages") as ArrayList<String>
                val name=it.getString("productName")
                val productSp=it.getString("productSp")
                val productDesc=it.getString("productDescription")
                binding.textView6.text = name
                binding.textView7.text = productSp
                binding.textView8.text = productDesc
                val slideList = ArrayList<SlideModel>()
                for (data in list){

                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))

                }

                cartAction(prodId,name,productSp ,it.getString("productCoverImg"))


                binding.imageSlider.setImageList(slideList)

            }.addOnFailureListener{
                Toast.makeText(this,  "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(prodId: String, name: String?, productSp: String?, coverImg: String?) {
        val productDao= AppDatabase.getInstance(this).productDao()

        if(productDao.isExit(prodId)!=null){
            binding.textView9.text="Go to Cart"
        }
        else{
            binding.textView9.text="Add to Cart"
        }

        binding.textView9.setOnClickListener{
            if(productDao.isExit(prodId)!=null){
                openCart()
            }
            else{
                addToCart(productDao,prodId,name,productSp,coverImg)
            }
        }
    }

    private fun addToCart(
        productDao: ProductDao,
        prodId: String,
        name: String?,
        productSp: String?,
        coverImg: String?
    ) {
        val data= ProductModel(prodId,name,coverImg,productSp)
        lifecycleScope.launch (Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView9.text="Go to Cart"
        }
    }

    private fun openCart() {
        val preference=this.getSharedPreferences("info", MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}
