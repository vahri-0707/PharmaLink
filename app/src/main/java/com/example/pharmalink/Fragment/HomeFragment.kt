package com.example.pharmalink.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.pharmalink.MenuBootomSheetFragment
import com.example.pharmalink.Notification_Bottom_Fragmet
import com.example.pharmalink.R
import com.example.pharmalink.adapter.MenuAdapter
import com.example.pharmalink.databinding.FragmentHomeBinding
import com.example.pharmalink.model.MenuItem
import com.example.pharmalink.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.widget.SearchView


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    private lateinit var originalMenuItems: List<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = Notification_Bottom_Fragmet()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }
        binding.viewAllBarang.setOnClickListener {
            val bottomSheetDialog = MenuBootomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        // Setup the SearchView
        setupSearchView()

        retrieveAndDisplayPopularItems()
        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Perform search
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Perform search as the user types
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String?) {
        if (query.isNullOrBlank()) {
            // If the query is null or blank, show all items
            setPopularItemsAdapter(originalMenuItems)
        } else {
            // Filter items based on the query (case-insensitive and search in the middle)
            val filteredItems = originalMenuItems.filter {
                it.drugName?.toLowerCase()?.contains(query.toLowerCase()) == true
            }
            setPopularItemsAdapter(filteredItems)
        }
    }





    private fun retrieveAndDisplayPopularItems() {
        database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Retrieve user data
        val userRef: DatabaseReference = database.reference.child("user").child(userId!!)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                user?.let {
                    // Set the user's name to the TextView with ID "name"
                    binding.name.text = "${it.name}"
                }

                // Now that you have the user's name, proceed to load and display other data
                loadAndDisplayOtherData()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun loadAndDisplayOtherData() {
        // Continue with the rest of your code to load and display other data...
        // For example, load and display popular items in the RecyclerView
        val drugRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        drugRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drugSnapshot in snapshot.children) {
                    val menuItem = drugSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }

                // Save the original menu items for later use in search
                originalMenuItems = menuItems.toList()

                randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun randomPopularItems() {
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())

        // Use a GridLayoutManager with 2 columns and vertical orientation
        val layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.PopularRecyclerView.layoutManager = layoutManager

        binding.PopularRecyclerView.adapter = adapter
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

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // Not implemented
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
