package com.example.pharmalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pharmalink.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drugName = intent.getStringExtra("MenuItemName")
        val drugImage = intent.getIntExtra("MenuItemImage", 0)
        binding.detailDrugName.text = drugName
        binding.DetailDrugImage.setImageResource(drugImage)

        binding.imageButton.setOnClickListener{
            finish()
        }
    }
}