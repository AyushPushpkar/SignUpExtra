package com.example.signup

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signup.databinding.ActivityUsernameProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class UsernameProfileActivity : AppCompatActivity() {

    private val binding: ActivityUsernameProfileBinding by lazy {
        ActivityUsernameProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        binding.btnprofileupdate.setOnClickListener {
            // Update profile
            updateProfile()
        }
    }

    private fun updateProfile(){

        auth.currentUser?.let { user ->
            val username = binding.etusername.text.toString()
            val photoURI = Uri.parse("android.resource://$packageName/${R.drawable.spidey}")
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoURI)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.updateProfile(profileUpdate).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                        Toast.makeText(this@UsernameProfileActivity, "Profile updated" , Toast.LENGTH_LONG).show()
                    }
                }
                catch (e :Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UsernameProfileActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun checkLoggedInState(){
        val user = auth.currentUser
        if(user ==  null){
            binding.tvloggedIn.text = "You are not logged in"
        }
        else{
            binding.tvloggedIn.text = "You are logged in"
            binding.etusername.setText(user.displayName)
           binding.imgprofile.setImageURI(user.photoUrl)
        }
    }
}