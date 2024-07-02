package com.example.signup

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signup.databinding.ActivityNavDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import android.Manifest
import android.util.Log

class NavDrawerActivity : AppCompatActivity() {
    private val binding: ActivityNavDrawerBinding by lazy {
        ActivityNavDrawerBinding.inflate(layoutInflater)
    }
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.Permissions.setOnClickListener {
            requestPermissions()
        }

        binding.tvIntent.setText(intent.getStringExtra("DATA_REC"))


        // Inflate the navigation drawer header layout
        val navigationView = binding.navview
        val headerView = navigationView.getHeaderView(0)

        // Find the TextView in the header layout
        val textViewEmail = headerView.findViewById<TextView>(R.id.loggedmail)

        // Retrieve the email information from FirebaseAuth
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email ?: "Unknown"

        // Update the TextView with the logged-in email
        textViewEmail.text = userEmail


        //toolbar
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val drawerLayout = binding.drawerlayout
        val navView = binding.navview

        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setIcon(R.drawable.cat)

        navView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.nav_signout -> {
                    val intent = Intent(this, RandomActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_login -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.firestorego -> {
                    val intent = Intent(this, FirestoreActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_vp -> {
                    val intent = Intent(this, ViewPagerActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_set -> {
                    val intent = Intent(this, UsernameProfileActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_notify -> {
                    val intent = Intent(this, NotificationActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }

    }

    private fun enableEdgeToEdge() {
        // Your edge-to-edge implementation
        // This typically involves setting flags or adjusting layout to handle system insets
    }


    //permissions

    private fun hasLocationPermission() =
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


    // Request necessary permissions
    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if(!hasLocationPermission()){
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionsToRequest.toTypedArray(),0)
        }
        Log.d("Permissions Request", "Permissions to request: $permissionsToRequest")
    }

    // Handle permission results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if( grantResults[i] ==PackageManager.PERMISSION_GRANTED){
                    Log.d("Permissions Request","${permissions[i]} granted.")
                }
                else {
                    Log.d("Permissions Request", "${permissions[i]} denied.")
                }
            }
        }
    }

}

