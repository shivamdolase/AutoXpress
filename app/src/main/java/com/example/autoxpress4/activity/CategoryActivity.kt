package com.example.autoxpress4.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.autoxpress4.R
import com.example.autoxpress4.adapter.CategoryProductAdapter
import com.example.autoxpress4.model.AddProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        getProducts(intent.getStringExtra("cat"))
    }

    private fun getProducts(category: String?) {
        val list =ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCaterory",category)
            .get().addOnSuccessListener{
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                val recyclerview=findViewById<RecyclerView>(R.id.recyclerView)
                recyclerview.adapter= CategoryProductAdapter(this,list)
            }
    }
}