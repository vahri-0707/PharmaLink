package com.example.pharmalink

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.pharmalink.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var drugName: String?= null
    private var drugImage: String?= null
    private var drugDescription: String?= null
    private var drugPrice: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}