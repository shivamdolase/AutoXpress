package com.example.autoxpress4.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autoxpress4.R
import com.example.autoxpress4.databinding.ActivityAddressBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences= this.getSharedPreferences("user", MODE_PRIVATE)

        loadUserInfo()

        binding.proceed.setOnClickListener{
            validateData(
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userArea.text.toString(),
                binding.userCity.text.toString(),
                binding.userpincode.text.toString(),
                binding.userState.text.toString()
            )

        }

    }

    private fun validateData(number: String, name: String, area: String, city: String, pincode: String, state: String) {
        if(number.isEmpty()||name.isEmpty()||state.isEmpty()||city.isEmpty()||area.isEmpty()||pincode.isEmpty()){
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
        }
        else{
            storeData(area,city,pincode,state)

        }

    }

    private fun storeData(area: String, city: String, pincode: String, state: String) {
        val map= hashMapOf<String,Any>()
        map["area"]=area
        map["city"]=city
        map["pincode"]=pincode
        map["state"]=state
        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map)
            .addOnSuccessListener {
                val intent= Intent(this, CheckoutActivity::class.java)
                intent.putExtra("totalCost",intent.getStringArrayExtra("productIds"))
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {
        val preferences= this.getSharedPreferences("user", MODE_PRIVATE)
        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userNumber"))
                binding.userArea.setText(it.getString("area"))
                binding.userCity.setText(it.getString("city"))
                binding.userpincode.setText(it.getString("pincode"))
                binding.userState.setText(it.getString("state"))
            }
            .addOnFailureListener{

            }
    }
}