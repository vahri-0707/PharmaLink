package com.example.pharmalink.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.PayOutActivity
import com.example.pharmalink.R
import com.example.pharmalink.adapter.CartAdapter
import com.example.pharmalink.databinding.FragmentCartBinding
import com.example.pharmalink.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var drugNames: MutableList<String>
    private lateinit var drugPrices: MutableList<String>
    private lateinit var drugDescriptions: MutableList<String>
    private lateinit var drugImageUri: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.buttonBackToHome.setOnClickListener {
            // Handle the button click event
            navigateToHome()
        }

        binding.cartBottomnav.setOnClickListener{
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val drugReference : DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        drugNames = mutableListOf()
        drugPrices = mutableListOf()
        drugImageUri = mutableListOf()
        quantity = mutableListOf()
        drugDescriptions = mutableListOf()

        drugReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drugSnapshot in snapshot.children){
                    val cartItems = drugSnapshot.getValue(CartItems::class.java)

                    cartItems?.drugName?.let { drugNames.add(it) }
                    cartItems?.drugPrice?.let { drugPrices.add(it) }
                    cartItems?.drugDescription?.let { drugDescriptions.add(it) }
                    cartItems?.drugImage?.let { drugImageUri.add(it) }
                    cartItems?.drugQuantity?.let { quantity.add(it) }
                }
                setAdapter()
            }

            private fun setAdapter() {
                val adapter = CartAdapter(requireContext(), drugNames, drugPrices, drugDescriptions, drugImageUri, quantity)
                binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "data not fetch", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun navigateToHome() {
        // Use NavController to navigate back to the home screen
        findNavController().navigate(R.id.homeFragment)
    }

    companion object {

    }
}