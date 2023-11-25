package com.example.pharmalinkadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalinkadmin.adapter.OrderDetailsAdapter
import com.example.pharmalinkadmin.databinding.ActivityOrderDetailsBinding
import com.example.pharmalinkadmin.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {

    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var drugNames: ArrayList<String> = arrayListOf()
    private var drugImages: ArrayList<String> = arrayListOf()
    private var drugQuantity: ArrayList<Int> = arrayListOf()
    private var drugPrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //TOMBOL BACK BELUM DIBIKIN


        binding.backButton.setOnClickListener{
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {

        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->

                userName = receivedOrderDetails.userName
                drugNames = receivedOrderDetails.drugNames as ArrayList<String>
                drugImages = receivedOrderDetails.drugImages  as ArrayList<String>
                drugQuantity = receivedOrderDetails.drugQuantities  as ArrayList<Int>
                address = receivedOrderDetails.address
                phoneNumber = receivedOrderDetails.phoneNumber
                drugPrices = receivedOrderDetails.drugPrices  as ArrayList<String>
                totalPrice = receivedOrderDetails.totalPrice

                setUserDetail()
                setAdapter()

        }


    }


    private fun setUserDetail() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalPay.text = totalPrice
    }

    private fun setAdapter() {
        binding.orderDetailRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =  OrderDetailsAdapter(this,drugNames,drugImages,drugQuantity,drugPrices)
        binding.orderDetailRecyclerView.adapter = adapter

    }
}