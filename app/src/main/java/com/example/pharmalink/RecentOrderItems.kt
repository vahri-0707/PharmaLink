package com.example.pharmalink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.Fragment.CartFragment
import com.example.pharmalink.Fragment.HistoryFragment
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

        val recentOrderItems =
            intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>?
        recentOrderItems?.let { orderDetailsList ->
            if (orderDetailsList.isNotEmpty()) {
                val mostRecentOrderDetails = orderDetailsList.last()

                allDrugNames = mostRecentOrderDetails.drugNames as ArrayList<String>
                allDrugImages = mostRecentOrderDetails.drugImages as ArrayList<String>
                allDrugPrices = mostRecentOrderDetails.drugPrices as ArrayList<String>
                allDrugQuantities = mostRecentOrderDetails.drugQuantities as ArrayList<Int>
            }
        }

        setAdapter()

        binding.buttonBackToHistory.setOnClickListener {
            // Navigate to FragmentCart
            val intent = Intent(this, CartFragment::class.java)
            startActivity(intent)
            finish() // Optional: Finish the current activity if you don't want to go back to it
        }
    }

    private fun setAdapter() {
        val rv = binding.recyclerViewRecentBuy
        rv.layoutManager = LinearLayoutManager(this)
        val adapter =
            RecentBuyAdapter(this, allDrugNames, allDrugImages, allDrugPrices, allDrugQuantities)
        rv.adapter = adapter
    }
}

