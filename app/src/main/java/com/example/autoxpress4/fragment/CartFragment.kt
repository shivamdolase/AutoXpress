package com.example.autoxpress4.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.autoxpress4.activity.AddressActivity
import com.example.autoxpress4.adapter.CartAdapter
import com.example.autoxpress4.databinding.FragmentCartBinding
import com.example.autoxpress4.roomdb.AppDatabase
import com.example.autoxpress4.roomdb.ProductModel

class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var list:ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(layoutInflater)

        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()
        val dao= AppDatabase.getInstance(requireContext()).productDao()

        list=ArrayList()
        val adapter = CartAdapter(requireContext(), emptyList())
        binding.cartRecycler.adapter = adapter
//        if(binding.cartRecycler.adapter != null) {
//            Toast.makeText(requireContext(), "yes", Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(requireContext(), "no", Toast.LENGTH_LONG).show();
//
//        }
//        dao.getAllProducts().observe(requireActivity()){
//            binding.cartRecycler.adapter=CartAdapter(requireContext(),it)
//
//            adapter.updateData(it)
//            list.clear()
//            for(data in it){
//                list.add(data.productId)
//            }
//            totalCost(it)
//        }
        dao.getAllProducts().observe(viewLifecycleOwner) { products ->
            if (binding.cartRecycler.adapter == null) {
                binding.cartRecycler.adapter = CartAdapter(requireContext(), products)
            } else {
                adapter.updateData(products)
            }
            list.clear()
            for (data in products) {
                list.add(data.productId)
            }
            totalCost(products)
        }

        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!){
            total += item.productSp!!.toInt()
        }

        binding.textView12.text= "Total item in cart is ${data.size}"
        binding.textView13.text= "Total Cost: $total"

        binding.checkout.setOnClickListener{
            val intent= Intent(context, AddressActivity::class.java)
            val b=Bundle()
            b.putStringArrayList("productIds",list)
            b.putString("totalCost",total.toString())
            //intent.putExtra("totalCost",total.toString())
            //intent.putStringArrayListExtra("productIds",list)
            intent.putExtras(b)
            startActivity(intent)
        }
    }

}