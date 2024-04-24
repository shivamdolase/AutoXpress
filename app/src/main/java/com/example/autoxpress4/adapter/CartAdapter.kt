package com.example.autoxpress4.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.autoxpress4.activity.ProductDetailActivity
import com.example.autoxpress4.databinding.LayoutCartItemBinding
import com.example.autoxpress4.roomdb.AppDatabase
import com.example.autoxpress4.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context, var list: List<ProductModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = list[position]
        Glide.with(context).load(currentItem.productImage).into(holder.binding.imageView4)
        holder.binding.textView10.text = currentItem.productName
        holder.binding.textView11.text = currentItem.productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", currentItem.productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.imageView5.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(currentItem)
            }
        }
    }

    fun updateData(newList: List<ProductModel>) {
        list = newList
        notifyDataSetChanged()
    }
}

