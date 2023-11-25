package com.example.pharmalink.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pharmalink.databinding.BuyAgainItemBinding

class BuyAgainAdapter(
    private val buyAgainDrugName: MutableList<String>,
    private val buyAgainDrugPrice: MutableList<String>,
    private val buyAgainDrugImage: MutableList<String>,
    private var requireContext: Context
) : RecyclerView
.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(
            buyAgainDrugName[position],
            buyAgainDrugPrice[position],
            buyAgainDrugImage[position]
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =
            BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = buyAgainDrugName.size
    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind(drugName: String, drugPrice: String, drugImage: String) {
            binding.buyAgainDrugName.text = drugName
            binding.buyAgainDrugPrice.text = drugPrice

            val uriString = drugImage
            val uri = Uri.parse(uriString)
            Glide.with(requireContext).load(uri).into(binding.buyAgainDrugImage)
        }

    }

}