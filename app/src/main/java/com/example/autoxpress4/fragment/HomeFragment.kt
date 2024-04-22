package com.example.autoxpress4.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.autoxpress4.R
import com.example.autoxpress4.activity.ProductDetailActivity
import com.example.autoxpress4.adapter.CategoryAdapter
import com.example.autoxpress4.adapter.ProductAdapter
import com.example.autoxpress4.databinding.FragmentHomeBinding
//import com.example.autoxpress4.databinding.LayoutProductItemBinding
import com.example.autoxpress4.model.AddProductModel
import com.example.autoxpress4.model.CategoryModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class HomeFragment : Fragment() {

   // private lateinit var binding2: LayoutProductItemBinding
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        //binding2=LayoutProductItemBinding.inflate(layoutInflater)
       // binding.button3.setOnClickListener{
         //   val intent= Intent(requireContext(), ProductDetailActivity::class.java)
           // startActivity(intent)
        //}
        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if(preference.getBoolean("isCart",false))
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)

        getCategories()
        getProducts()
        getSliderImage()
        return binding.root
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)
            }

    }

    private fun getProducts() {
        val list =ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener{
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.productRecycler.adapter= ProductAdapter(requireContext(),list)
            }
    }

    private fun getCategories() {
        val list =ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener{
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter= CategoryAdapter(requireContext(),list)
            }
    }


}
