package com.example.pharmalink.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pharmalink.MainActivity
import com.example.pharmalink.RecentOrderItems
import com.example.pharmalink.adapter.BuyAgainAdapter
import com.example.pharmalink.databinding.FragmentHistoryBinding
import com.example.pharmalink.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.math.BigDecimal

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        retrieveBuyHistory()

        binding.recentbuyitem.setOnClickListener{
            seeItemsRecentBuy()
        }

        binding.buttonBackToHomee.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.receivedButton.setOnClickListener{
            updateOrdefStatus()
        }

        return binding.root
    }

    private fun updateOrdefStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        val intent = Intent(requireContext(), RecentOrderItems::class.java)
        intent.putExtra("RecentBuyOrderItem", listOfOrderItem)
        startActivity(intent)
    }

    private fun retrieveBuyHistory() {
        binding.recentbuyitem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentbuyitem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()

        recentOrderItem?.let {
            val totalAmount = calculateTotalAmount(it)

            with(binding) {
                buyAgainDrugName.text = "${it.drugNames?.joinToString(", ") ?: ""}"

                // Convert totalAmount to a string and remove trailing .0
                buyAgainDrugPrice.text = "Total : Rp. ${totalAmount.stripTrailingZeros().toPlainString()}"

                val image = it.drugImages?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainDrugImage)

                val isOrderIsAccepted = listOfOrderItem[0].orderAccepted
                if (isOrderIsAccepted){
                    orderedStatus.background.setTint(Color.GREEN)
                    receivedButton.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun calculateTotalAmount(orderDetails: OrderDetails): BigDecimal {
        return orderDetails.drugPrices?.map {
            it.replace("Rp. ", "").replace(",", "").toDoubleOrNull() ?: 0.0
        }?.let {
            it.sumOf { price -> BigDecimal.valueOf(price) }
        } ?: BigDecimal.ZERO
    }


    private fun setPreviousBuyItemRecyclerView() {
        val buyAgainDrugName = mutableListOf<String>()
        val buyAgainDrugPrice = mutableListOf<String>()
        val buyAgainDrugImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].drugNames?.firstOrNull()?.let {
                buyAgainDrugName.add(it)
                listOfOrderItem[i].drugPrices?.firstOrNull()?.let {
                    buyAgainDrugPrice.add(it)
                    listOfOrderItem[i].drugImages?.firstOrNull()?.let {
                        buyAgainDrugImage.add(it)
                    }
                }
                val rv = binding.BuyAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = BuyAgainAdapter(
                    buyAgainDrugName,
                    buyAgainDrugPrice,
                    buyAgainDrugImage,
                    requireContext()
                )
                rv.adapter = buyAgainAdapter
            }
        }
    }

}