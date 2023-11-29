package com.example.pharmalinkadmin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.pharmalinkadmin.databinding.ActivityMainBinding
import com.example.pharmalinkadmin.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()  // Initialize FirebaseAuth

        loadUserProfileData()

        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.outForDeliveryButton.setOnClickListener{
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener{
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }


        binding.pendingOrderButton.setOnClickListener{
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            // Clear the stored credentials
            val editor = getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit()
            editor.remove("email")
            editor.remove("password")
            editor.apply()

            // Perform Firebase logout
            auth.signOut()

            // Redirect to the login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }

    private fun loadUserProfileData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = FirebaseDatabase.getInstance().reference.child("user").child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.child("userName").getValue(String::class.java)
                        val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                        // Set username to the TextView if not null
                        if (!userName.isNullOrEmpty()) {
                            binding.userName.text = "Hello $userName!"
                        }

                        // Load the user image using Glide if the URL is not null
                        if (!imageUrl.isNullOrEmpty()) {
                            Glide.with(this@MainActivity)
                                .load(imageUrl)
                                .into(binding.userImage)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()
        completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children){
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    
                    completeOrder?.totalPrice?.replace("$", "")?.toIntOrNull()
                        ?.let { i ->
                            listOfTotalPay.add(i)
                        }
                }
                
                binding.wholeTimeEarning.text = "Rp. " + listOfTotalPay.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun completedOrders() {
        val completeOrderReference = database.reference.child("CompletedOrder")
        var completeOrderItemCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrders.text = completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}