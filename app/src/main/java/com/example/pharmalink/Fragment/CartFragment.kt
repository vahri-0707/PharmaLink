package com.example.pharmalink.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.R
import com.example.pharmalink.adapter.CartAdapter
import com.example.pharmalink.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val cartDrugName = listOf("Paracetamol","Procold","Bodrex","Amoxcilin")
        val cartItemPrice = listOf("Rp. 5000", "Rp. 7000", "Rp. 5000", "Rp. 15000")
        val cartImage = listOf(
            R.drawable.medicine_paracetamol,
            R.drawable.medicine_procold,
            R.drawable.medicine_bodrex,
            R.drawable.medicine_amoxiline)

        val adapter = CartAdapter(ArrayList(cartDrugName), ArrayList(cartItemPrice), ArrayList(cartImage))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        binding.buttonBackToHome.setOnClickListener {
            // Handle the button click event
            navigateToHome()
        }

        return binding.root
    }

    private fun navigateToHome() {
        // Use NavController to navigate back to the home screen
        findNavController().navigate(R.id.homeFragment)
    }

    companion object {

    }
}