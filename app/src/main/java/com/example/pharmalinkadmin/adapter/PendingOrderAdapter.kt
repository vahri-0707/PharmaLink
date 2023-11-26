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
    private val listOfName: MutableList<String>,
    private val listOfTotalPrice: MutableList<String>,
    private val listOfImageFirstDrugOrder: MutableList<String>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    private val acceptedStatusList: MutableList<Boolean> = MutableList(listOfName.size) { false }

    interface OnItemClicked {
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

    override fun getItemCount(): Int = listOfName.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            binding.apply {
                customerName.text = listOfName[position]
                pendingOrderQuantity.text = listOfTotalPrice[position]
                val uriString = listOfImageFirstDrugOrder[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderDrugImage)

                orderedAcceptButton.apply {
                    if (!acceptedStatusList[position]) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }

                    setOnClickListener {
                        if (!acceptedStatusList[position]) {
                            text = "Dispatch"
                            acceptedStatusList[position] = true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            listOfName.removeAt(position)
                            listOfTotalPrice.removeAt(position)
                            listOfImageFirstDrugOrder.removeAt(position)
                            acceptedStatusList.removeAt(position)
                            notifyItemRemoved(position)
                            showToast("Order is dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }

                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}

