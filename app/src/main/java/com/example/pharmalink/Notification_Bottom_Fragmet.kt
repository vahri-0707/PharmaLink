package com.example.pharmalink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.adapter.NotificationAdapter
import com.example.pharmalink.databinding.FragmentNotificationBottomFragmetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Notification_Bottom_Fragmet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomFragmetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomFragmetBinding.inflate(layoutInflater,container,false)
        val notifications = listOf("Your order has been canceled successfully", "Order has been taken by the driver","congrats your order placed")
        val notificationImages = listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)
        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}