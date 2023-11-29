package com.example.pharmalinkadmin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pharmalinkadmin.databinding.ActivityLoginBinding
import com.example.pharmalinkadmin.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        if (isLoggedIn()) {
            startMainActivity()
        }

        binding.loginButton.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill All details", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(email, password)
            }
        }

        binding.goToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveCredentials(email, password)
                updateUi(auth.currentUser)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveCredentials(email, password)
                        saveUserData()
                        updateUi(auth.currentUser)
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(email, password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            // Save user data to Firebase
            // Note: You might want to modify this based on your database structure
            // database.child("user").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startMainActivity()
        finish()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveCredentials(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun isLoggedIn(): Boolean {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        return !email.isNullOrBlank() && !password.isNullOrBlank()
    }
}
