package com.example.pharmalinkadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalinkadmin.adapter.DeliveryAdapter
import com.example.pharmalinkadmin.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }


        val customerName = arrayListOf(
            "Rabby Ahsan",
            "Yoangga Achmad",
            "Hilqudz Dzikri",
        )
        val moneyStatus = arrayListOf(
            "Recieved",
            "Not Recieved",
            "Pending",
        )
        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}