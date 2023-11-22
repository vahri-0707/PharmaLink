package com.example.pharmalink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pharmalink.Fragment.CartFragment
import com.example.pharmalink.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {

    lateinit var binding : ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackToCart.setOnClickListener{
            val intent = Intent(this, CartFragment::class.java)
            startActivity(intent)
        }

        binding.PlaceMyOrder.setOnClickListener{
            val bottomSheetDeialog = CongratsBottomSheet()
            bottomSheetDeialog.show(supportFragmentManager, "Test")
        }
    }
}