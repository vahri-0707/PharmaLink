package com.example.pharmalink.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmalink.databinding.PopularItemBinding

class PopularAdapter (private val items:List<String>, private val price:List<String>, private val image:List<Int>) : RecyclerView.Adapter<PopularAdapter.PouplerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PouplerViewHolder {
        return PouplerViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PouplerViewHolder, position: Int) {
        val item = items[position]
        val price = price[position]
        val images = image[position]
        holder.bind(item, price, images)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PouplerViewHolder (private val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root){
        private val imagesView = binding.imageView2
        fun bind(item: String, price: String, images: Int) {
            binding.barangPopular.text = item
            binding.pricePopular.text = price
            imagesView.setImageResource(images)
        }

    }
}