package com.example.pharmalinkadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmalinkadmin.adapter.AddItemAdapter
import com.example.pharmalinkadmin.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        //!!!
        binding.backButton.setOnClickListener{
            finish()
        }

        val adapter = AddItemAdapter(ArrayList(menuDrugName), ArrayList(menuItemPrice), ArrayList(menuImage))
        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter
    }
}