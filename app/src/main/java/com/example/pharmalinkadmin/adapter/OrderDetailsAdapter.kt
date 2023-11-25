package com.example.pharmalinkadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pharmalinkadmin.databinding.ActivityOrderDetailsBinding
import com.example.pharmalinkadmin.databinding.OrderDetailItemBinding
import com.example.pharmalinkadmin.model.OrderDetails

class OrderDetailsAdapter(
    private var context: Context,
    private var drugNames: ArrayList<String>,
    private var drugImages: ArrayList<String>,
    private var drugQuantitys: ArrayList<Int>,
    private var drugPrices: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderDetailItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }



    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = drugNames.size

    inner class OrderDetailsViewHolder(private val binding: OrderDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                drugName.text = drugNames[position]
                drugQuantity.text = drugQuantitys[position].toString()
                val uriString = drugImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(drugImage)
                drugPrice.text = drugPrices[position]
            }
        }

    }
}