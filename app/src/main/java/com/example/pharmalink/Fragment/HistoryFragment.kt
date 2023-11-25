package com.example.pharmalink.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pharmalink.MainActivity
import com.example.pharmalink.PayOutActivity
import com.example.pharmalink.R
import com.example.pharmalink.adapter.BuyAgainAdapter
import com.example.pharmalink.databinding.FragmentHistoryBinding
import com.example.pharmalink.model.OrderDetails
import com.example.pharmalink.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

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

        return binding.root
    }

    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), recentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem", recentBuy)
            startActivity(intent)
        }
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
            with(binding) {
                buyAgainDrugName.text = it.drugNames?.firstOrNull() ?: ""
                buyAgainDrugPrice.text = it.drugPrices?.firstOrNull() ?: ""
                val image = it.drugImages?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainDrugImage)

                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {

                }
            }
        }

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