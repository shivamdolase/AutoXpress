package com.example.autoxpress4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.autoxpress4.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentMoreBinding
    //private lateinit var list:ArrayList<AllOrderModel>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMoreBinding.inflate(layoutInflater)
//        list= ArrayList()
//
//        val preferences=requireContext().getSharedPreferences("user",AppCompactActivity.MODE_PRIVATE)
//
//
//
//        Firebase.firestore.collection("allOrders")
//            .whereEqualTo("userId",preferences.getString("number",""))
//            .get().addOnSuccessListener {
//            list.clear()
//            for(doc in it){
//                val data=doc.toObject(AllOrderModel::class.java)
//                list.add(data)
//            }
//            binding.recyclerView.adapter= AllOrderAdapter(list,requireContext())
//        }
       return binding.root
    }

}