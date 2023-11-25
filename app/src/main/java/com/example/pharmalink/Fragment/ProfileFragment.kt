package com.example.pharmalink.Fragment


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pharmalink.databinding.FragmentProfileBinding
import com.example.pharmalink.model.UserModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import com.bumptech.glide.Glide


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var displayedImage: ShapeableImageView
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        displayedImage = binding.displayedImage



        // Initialize UI components and set user data
        initUI()
        setUserData()

        return binding.root
    }

    private fun initUI() {
        binding.placeImage.setOnClickListener {
            openImagePicker()
        }

        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val address = binding.address.text.toString()

            updateUserData(name, email, phone, address)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
                displayedImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUserData(name: String, email: String, phone: String, address: String) {
        if (imageUri != null) {
            uploadImageAndSetUserProfile(imageUri!!, name, email, phone, address)
        } else {
            updateDatabaseUserData(name, email, phone, address, "")
        }
    }

    private fun uploadImageAndSetUserProfile(imageUri: Uri, name: String, email: String, phone: String, address: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val storageReference = storage.reference.child("profile_images/$userId.jpg")

            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        updateDatabaseUserData(name, email, phone, address, uri.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun updateDatabaseUserData(name: String, email: String, phone: String, address: String, imageUrl: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "address" to address,
                "imageUrl" to imageUrl
            )

            userReference.setValue(userData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile Update Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.name.setText(userProfile.name)
                            binding.email.setText(userProfile.email)
                            binding.phone.setText(userProfile.phone)
                            binding.address.setText(userProfile.address)

                            if (snapshot.hasChild("imageUrl")) {
                                val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                                // Load the image using Glide
                                if (!imageUrl.isNullOrBlank()) {
                                    Glide.with(requireContext())
                                        .load(imageUrl)
                                        .into(binding.displayedImage)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancellation if needed
                }
            })
        }
    }
}
