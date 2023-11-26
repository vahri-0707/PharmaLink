package com.example.pharmalinkadmin.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pharmalinkadmin.PendingOrderActivity
import com.example.pharmalinkadmin.databinding.PendingOrderItemBinding


class PendingOrderAdapter(
    private val context: PendingOrderActivity,
    private val customerNames: MutableList<String>,
    private val quantity: MutableList<String>,
    private val drugImage: MutableList<String>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isAccepted = false

        fun bind(position: Int) {

            binding.apply {
                customerName.text = customerNames[position]
                pendingOrderQuantity.text = quantity[position]
                var uriString = drugImage[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderDrugImage)

                orderedAcceptButton.apply {
                    if (!isAccepted) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }

                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }

                }
                itemView.setOnClickListener{
                    itemClicked.onItemClickListener(position)
                }

            }

        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }

    }
}