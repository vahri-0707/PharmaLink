package com.example.pharmalink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pharmalink.Fragment.CartFragment
import com.example.pharmalink.databinding.ActivityPayOutBinding
import com.example.pharmalink.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.temporal.TemporalAmount

class PayOutActivity : AppCompatActivity() {

    lateinit var binding : ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var totalAmount: String
    private lateinit var drugItemName: ArrayList<String>
    private lateinit var drugItemPrice: ArrayList<String>
    private lateinit var drugItemImage: ArrayList<String>
    private lateinit var drugItemDescription: ArrayList<String>
    private lateinit var drugItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        setUserData()

        val intent = intent
        drugItemName = intent.getStringArrayListExtra("DrugItemName") as ArrayList<String>
        drugItemPrice= intent.getStringArrayListExtra("DrugItemPrice") as ArrayList<String>
        drugItemImage = intent.getStringArrayListExtra("DrugItemImage") as ArrayList<String>
        drugItemDescription = intent.getStringArrayListExtra("DrugItemDescription") as ArrayList<String>
        drugItemQuantities = intent.getIntegerArrayListExtra("DrugItemQuantities") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString()

        // binding.totalAmount.isEnabled = false
        binding.totalAmount.setText("Rp. $totalAmount")


        binding.backeButton.setOnClickListener{
            finish()
        }

        binding.PlaceMyOrder.setOnClickListener{
            name = binding.name.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            address = binding.address.text.toString().trim()

            if (name.isBlank() && phone.isBlank() && address.isBlank()){
                Toast.makeText(this, "Please Enter All The Details", Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }

        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId, name, drugItemName, drugItemPrice, drugItemImage, drugItemQuantities, address, totalAmount,  phone, time, itemPushKey, false,false)
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener{
                Toast.makeText(this, "failed to order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0

        for (i in 0 until drugItemPrice.size) {
            var price = drugItemPrice[i]

            // Replace '$' with 'Rp'
            price = price.replace("$", "Rp")

            // Remove 'Rp' if it occurs at the beginning
            if (price.startsWith("Rp")) {
                price = price.substring(2)
            }

            // Now, convert the modified string to an integer
            val priceIntValue = price.toIntOrNull() ?: 0

            val quantity = drugItemQuantities[i]

            totalAmount += priceIntValue * quantity
        }

        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user!=null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""
                        val addresses = snapshot.child("address").getValue(String::class.java)?:""
                        binding.apply {
                            name.setText(names)
                            phone.setText(phones)
                            address.setText(addresses)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}