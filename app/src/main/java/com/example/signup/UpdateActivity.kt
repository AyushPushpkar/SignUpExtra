package com.example.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signup.databinding.ActivityUpdateBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class UpdateActivity : AppCompatActivity() {

    private val binding : ActivityUpdateBinding by lazy {
        ActivityUpdateBinding.inflate(layoutInflater)
    }
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = Firebase.firestore

        binding.nameupdate.setText( intent.getStringExtra("NAME"))
        binding.keyupdate.setText( intent.getStringExtra("PASS"))

        binding.updatebtn.setOnClickListener {

            val user = hashMapOf(
                "name" to binding.nameupdate.text.toString() ,
                "password" to binding.keyupdate.text.toString()
            )

            val documentId = intent.getStringExtra("ID")
            db.collection("users").document(documentId!!).set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Update failed: $e", Toast.LENGTH_SHORT).show()
                }
            finish()
        }

    }
}