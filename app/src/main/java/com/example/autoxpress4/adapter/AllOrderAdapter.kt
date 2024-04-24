package com.example.autoxpress_admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.autoxpress4.databinding.AllOrderItemLayoutBinding
import com.example.autoxpress4.model.AllOrderModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore



class AllOrderAdapter (val list:ArrayList<AllOrderModel>,val context: Context)
    :RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>(){
    inner class AllOrderViewHolder( val binding:AllOrderItemLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateStatus(str:String,doc:String){
        val data=hashMapOf<String,Any>()
        Firebase.firestore.collection("allOrders")
            .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context,"Status Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {

        holder.binding.productTitle.text=list[position].name
        holder.binding.productPrice.text=list[position].price


        when(list[position].status){
            "Ordered"->{
                holder.binding.productStatus.text="Ordered"
            }
            "Dispatched"->{
                holder.binding.productStatus.text="Dispatched"
            }
            "Delivered"->{
                holder.binding.productStatus.text="Delivered"
            }
            "Canceled"->{
                holder.binding.productStatus.text="Canceled"
            }

        }
    }

}