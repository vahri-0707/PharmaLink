package com.example.pharmalink.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmalink.databinding.BuyAgainItemBinding

class BuyAgainAdapter(private val buyAgainDrugName: List<String>, private val
buyAgainDrugPrice: List<String>, private val buyAgainDrugImage: List<Int>
) : RecyclerView
    .Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
holder.bind(buyAgainDrugName[position],buyAgainDrugPrice[position],buyAgainDrugImage[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = buyAgainDrugName.size
    class BuyAgainViewHolder(private val binding: BuyAgainItemBinding): RecyclerView.ViewHolder
        (binding.root){
        fun bind(drugName: String, drugPrice: String, drugImage: Int) {
            binding.buyAgainDrugName.text=drugName
            binding.buyAgainDrugPrice.text=drugPrice
            binding.buyAgainDrugImage.setImageResource(drugImage)
        }

    }

}