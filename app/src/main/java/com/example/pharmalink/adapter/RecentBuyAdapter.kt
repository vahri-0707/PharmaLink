package com.example.pharmalink.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pharmalink.databinding.RecentBuyItemBinding


class RecentBuyAdapter(private var context: Context,
    private val drugNameList: ArrayList<String>,
    private val drugImageList: ArrayList<String>,
    private val drugPriceList: ArrayList<String>,
    private val drugQuantityList: ArrayList<Int>
    ): RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentViewHolder(binding)
    }

    override fun getItemCount(): Int = drugNameList.size


    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class RecentViewHolder(private val binding: RecentBuyItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                drugName.text = drugNameList[position]
                val uriString= drugImageList[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(drugImage)
                drugPrice.text = drugPriceList[position]
                drugQuantity.text = drugQuantityList[position].toString()

            }

        }

    }
}