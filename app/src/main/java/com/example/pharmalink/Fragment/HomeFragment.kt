package com.example.pharmalink.Fragment

import android.os.Bundle
import android.os.TestLooperManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.pharmalink.MenuBootomSheetFragment
import com.example.pharmalink.Notification_Bottom_Fragmet
import com.example.pharmalink.R
import com.example.pharmalink.adapter.PopularAdapter
import com.example.pharmalink.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.notificationButton.setOnClickListener{
            val bottomSheetDialog = Notification_Bottom_Fragmet()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        binding.viewAllBarang.setOnClickListener{
            val bottomSheetDialog = MenuBootomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner_1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner_3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object :ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
        val namaBarang = listOf("Paracetamol","Procold","Bodrex","Amoxcilin")
        val Price = listOf("Rp. 5000", "Rp. 7000", "Rp. 5000", "Rp. 15000")
        val fotoBarangTerlarisImages = listOf(R.drawable.medicine_paracetamol, R.drawable.medicine_procold, R.drawable.medicine_bodrex, R.drawable.medicine_amoxiline)

        val adapter = PopularAdapter(namaBarang, Price, fotoBarangTerlarisImages, requireContext())


        // Use a GridLayoutManager with 2 columns
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.PopularRecyclerView.layoutManager = layoutManager

        binding.PopularRecyclerView.adapter = adapter


        }

    companion object {
    }
}