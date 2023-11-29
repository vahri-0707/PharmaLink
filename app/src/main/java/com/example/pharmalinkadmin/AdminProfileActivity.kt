package com.example.pharmalinkadmin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pharmalinkadmin.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    private val storage = FirebaseStorage.getInstance()
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        binding.backButton.setOnClickListener {
            finish()
        }

        // Initialize UI components and set user data
        initUI()

        // Disable editing initially
        disableEditing()

        // Retrieve user data from Firebase
        retrieveUserData()
    }

    private fun initUI() {
        binding.placeImage.setOnClickListener {
            openImagePicker()
        }

        binding.saveInfoButton.setOnClickListener {
            updateUserData()
        }

        binding.editButton.setOnClickListener {
            toggleEditing()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun disableEditing() {
        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.saveInfoButton.isEnabled = false
    }

    private fun toggleEditing() {
        val isEnable = !binding.name.isEnabled
        binding.name.isEnabled = isEnable
        binding.address.isEnabled = isEnable
        binding.email.isEnabled = isEnable
        binding.phone.isEnabled = isEnable
        binding.password.isEnabled = isEnable

        if (isEnable) {
            binding.name.requestFocus()
        }

        binding.saveInfoButton.isEnabled = isEnable
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("userName").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val password = snapshot.child("password").getValue(String::class.java)
                        val address = snapshot.child("address").getValue(String::class.java)
                        val phone = snapshot.child("phone").getValue(String::class.java)
                        val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                        setDataToTextView(ownerName, email, password, address, phone)

                        if (!imageUrl.isNullOrBlank()) {
                            // Load the image using Glide
                            Glide.with(this@AdminProfileActivity)
                                .load(imageUrl)
                                .into(binding.displayedImage)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun setDataToTextView(
        ownerName: String?,
        email: String?,
        password: String?,
        address: String?,
        phone: String?
    ) {
        binding.name.setText(ownerName)
        binding.email.setText(email)
        binding.password.setText(password)
        binding.phone.setText(phone)
        binding.address.setText(address)
    }

    private fun updateUserData() {
        val updateName = binding.name.text.toString()
        val updateEmail = binding.email.text.toString()
        val updatePhone = binding.phone.text.toString()
        val updateAddress = binding.address.text.toString()
        val updatePassword = binding.password.text.toString()

        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.child("userName").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)
            userReference.child("password").setValue(updatePassword)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                    auth.currentUser?.updateEmail(updateEmail)
                    auth.currentUser?.updatePassword(updatePassword)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT).show()
                }

            if (imageUri != null) {
                // Upload image to Firebase Storage and save URL in the database
                uploadImageToStorage(currentUserUid, imageUri!!)
            }
        } else {
            Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToStorage(uid: String, imageUri: Uri) {
        val imageRef = storage.reference.child("$uid/profile_image.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the image URL in the database
                    saveImageUrlToDatabase(uid, uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToDatabase(uid: String, imageUrl: String) {
        val userReference = adminReference.child(uid)
        userReference.child("imageUrl").setValue(imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Image URL saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save image URL", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.displayedImage) // Display the selected image in displayedImage
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
