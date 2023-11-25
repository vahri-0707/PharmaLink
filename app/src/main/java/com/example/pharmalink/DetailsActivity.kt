package com.example.pharmalink

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pharmalink.databinding.ActivityDetailsBinding
import com.example.pharmalink.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var drugName: String?= null
    private var drugImage: String?= null
    private var drugDescription: String?= null
    private var drugPrice: String?= null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        drugName = intent.getStringExtra("MenuItemName")
        drugDescription = intent.getStringExtra("MenuItemDescription")
        drugPrice = intent.getStringExtra("MenuItemPrice")
        drugImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailDrugName.text = drugName
            detailDescription.text = drugDescription
            detailDrugPrice.text = drugPrice
            Glide.with(this@DetailsActivity).load(Uri.parse(drugImage)).into(detailDrugImage)

        }

        binding.imageButton.setOnClickListener{
            finish()
        }

        binding.addItemButton.setOnClickListener{
            addItemToCart()
        }

    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        val cartItem = CartItems(drugName.toString(), drugPrice.toString(), drugDescription.toString(), drugImage.toString(), 1)
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Items added into cart successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Item Not added", Toast.LENGTH_SHORT).show()
        }

    }
}