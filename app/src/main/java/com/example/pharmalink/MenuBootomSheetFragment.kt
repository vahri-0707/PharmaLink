package com.example.pharmalink

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.adapter.MenuAdapter
import com.example.pharmalink.databinding.FragmentMenuBootomSheetBinding
import com.example.pharmalink.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBootomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBootomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        retrieveMenuItems()

        // Use GridLayoutManager with 2 columns
        val layoutManager = GridLayoutManager(requireContext(), 2)

        return binding.root

    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val drugRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        drugRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drugSnapShot in snapshot.children) {
                    val menuItem = drugSnapShot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("ITEMS", "onDataChane: Data Recieved")
                setAdapter()
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setAdapter() {
        if (menuItems.isNotEmpty()){
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter
            Log.d("ITEMS", "setAdapter: data set")
        } else{
            Log.d("ITEMS", "setAdapter: data NOt set")
        }

    }

    companion object {

    }

}