package com.example.pharmalink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalink.adapter.MenuAdapter
import com.example.pharmalink.databinding.FragmentMenuBootomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBootomSheetFragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentMenuBootomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater,container,false)

        binding.buttonBack.setOnClickListener {
            dismiss()
        }

        val menuDrugName = listOf("Paracetamol","Procold","Bodrex","Amoxcilin","Procold","Bodrex")
        val menuItemPrice = listOf("Rp. 5000", "Rp. 7000", "Rp. 5000", "Rp. 15000", "Rp. 7000", "Rp. 5000")
        val menuImage = listOf(
            R.drawable.medicine_paracetamol,
            R.drawable.medicine_procold,
            R.drawable.medicine_bodrex,
            R.drawable.medicine_amoxiline,
            R.drawable.medicine_procold,
            R.drawable.medicine_bodrex,
        )

        val adapter = MenuAdapter(ArrayList(menuDrugName), ArrayList(menuItemPrice), ArrayList(menuImage))
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root

    }

    companion object {

    }
}