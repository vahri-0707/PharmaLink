package com.example.pharmalink.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pharmalink.R
import com.example.pharmalink.databinding.FragmentProfileBinding
import com.example.pharmalink.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        setUserData()

        binding.saveInfoButton.setOnClickListener{
            val name= binding.name.text.toString()
            val email= binding.email.text.toString()
            val phone= binding.phone.text.toString()
            val address= binding.address.text.toString()

            updateUserData(name,email,phone,address)
        }


        return binding.root
    }

    private fun updateUserData(name: String, email: String, phone: String, address: String) {
        val userId = auth.currentUser?.uid
        if (userId != null)
        {
            val userReference = database.getReference("user").child(userId)

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "address" to address
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Update Successfully",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),"Profile Update Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId!=null){
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object  :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile!=null){
                            binding.name.setText(userProfile.name)
                            binding.email.setText(userProfile.email)
                            binding.phone.setText(userProfile.phone)
                            binding.address.setText(userProfile.address)

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