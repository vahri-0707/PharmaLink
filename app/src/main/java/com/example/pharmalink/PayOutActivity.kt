package com.example.pharmalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pharmalink.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {

    lateinit var binding : ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.PlaceMyOrder.setOnClickListener{
            val bottomSheetDeialog = CongratsBottomSheet()
            bottomSheetDeialog.show(supportFragmentManager, "Test")
        }
    }
}