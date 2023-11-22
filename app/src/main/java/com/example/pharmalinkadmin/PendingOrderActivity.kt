package com.example.pharmalinkadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalinkadmin.adapter.PendingOrderAdapter
import com.example.pharmalinkadmin.databinding.ActivityPendingOrderBinding
import com.example.pharmalinkadmin.databinding.PendingOrderItemBinding

class PendingOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val orderedCustomerName = arrayListOf(
            "Vahri",
            "Yoangga",
            "Ahsan",
            "Kiki",
        )
        val orderedQuantity = arrayListOf(
            "8",
            "6",
            "3",
            "9",
        )
        val orderedDrugImage = arrayListOf(
            R.drawable.medicine_amoxiline,
            R.drawable.medicine_bodrex,
            R.drawable.medicine_paracetamol,
            R.drawable.medicine_procold,
        )

        val adapter = PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedDrugImage, this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}