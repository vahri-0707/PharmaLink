package com.example.pharmalink.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.R
import com.example.pharmalink.adapter.BuyAgainAdapter
import com.example.pharmalink.databinding.FragmentHistoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView(){
        val buyAgainDrugName = listOf("Paracetamol","Procold","Bodrex","Amoxcilin")
        val buyAgainDrugPrice = listOf("Rp. 5000", "Rp. 7000", "Rp. 5000", "Rp. 15000")
        val buyAgainDrugImage = listOf(
            R.drawable.medicine_paracetamol,
            R.drawable.medicine_procold,
            R.drawable.medicine_bodrex,
            R.drawable.medicine_amoxiline)
        buyAgainAdapter = BuyAgainAdapter(buyAgainDrugName,buyAgainDrugPrice,buyAgainDrugImage)
        binding.BuyAgainRecyclerView.adapter = buyAgainAdapter
        binding.BuyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}