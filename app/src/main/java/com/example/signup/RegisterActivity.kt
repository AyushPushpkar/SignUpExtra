package com.example.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signup.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.imageView11.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.textView13.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //initialise firebase auth
        auth = FirebaseAuth.getInstance()
        binding.button.setOnClickListener {
            //get text from edittext
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val repeat = binding.password2.text.toString()

            //check if blank
            if (email.isEmpty() || password.isEmpty() || repeat.isEmpty()) {
                Toast.makeText(this, "Please fill all the Details", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else if (repeat != password) {
                Toast.makeText(this, "Repeat password must be same", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Send email verification
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        Toast.makeText(this, "Registration successful. Verification email sent.", Toast.LENGTH_SHORT).show()
                                        // Navigate to verification activity
                                        val intent = Intent(this, VerifyEmailActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to send verification email: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                        else {
                            // Handle exceptions
                            val exceptionMessage = task.exception?.localizedMessage ?: "Registration failed"
                            Toast.makeText(this, exceptionMessage, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    // Helper function to validate email format
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}