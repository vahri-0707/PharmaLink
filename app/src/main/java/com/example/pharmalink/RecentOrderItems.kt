package com.example.pharmalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.adapter.RecentBuyAdapter
import com.example.pharmalink.databinding.ActivityRecentOrderItemsBinding
import com.example.pharmalink.model.OrderDetails

class RecentOrderItems : AppCompatActivity() {

    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }

    private lateinit var allDrugNames: ArrayList<String>
    private lateinit var allDrugImages: ArrayList<String>
    private lateinit var allDrugPrices: ArrayList<String>
    private lateinit var allDrugQuantities: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>?
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]

                allDrugNames = recentOrderItem.drugNames as ArrayList<String>
                allDrugImages = recentOrderItem.drugImages as ArrayList<String>
                allDrugPrices = recentOrderItem.drugPrices as ArrayList<String>
                allDrugQuantities = recentOrderItem.drugQuantities as ArrayList<Int>
            }
        }

        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recyclerViewRecentBuy
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this,allDrugNames,allDrugImages,allDrugPrices,allDrugQuantities)
        rv.adapter = adapter
    }

}