package com.example.signup


import android.adservices.adid.AdId
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.signup.databinding.ActivityFirestoreBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.jar.Attributes.Name

class FirestoreActivity : AppCompatActivity() {
    private val binding: ActivityFirestoreBinding by lazy {
        ActivityFirestoreBinding.inflate(layoutInflater)
    }
    private var userListener: ListenerRegistration? = null
    private val list = arrayListOf<User>()
    private lateinit var rvAdapter: RvAdapter
    private val db: FirebaseFirestore = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        rvAdapter = RvAdapter(this, list)

        binding.recycleview.layoutManager = LinearLayoutManager(this)
        binding.recycleview.adapter = rvAdapter

        setupRealTimeListener()

        binding.button3.setOnClickListener{
            toggleListeners()
        }

    }


 /*   private fun loadAllUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                list.clear()
                for (document in result) {
                    val user = document.toObject(User::class.java)
                    user.id = document.id
                    list.add(user)
                }
//                rvAdapter.notifyDataSetChanged()
                rvAdapter.notifyItemInserted(list.size-1)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(this, "Failed to fetch users: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }                         */

    private fun setupRealTimeListener() {
        userListener?.remove() // Remove any existing listener

        userListener = db.collection("users")
            .orderBy("Name")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    list.clear()
                    for (document in snapshots.documents) {
                        val user = document.toObject(User::class.java)
                        user?.id = document.id
                        if (user != null) {
                            list.add(user)
                        }
                    }
                    rvAdapter.notifyDataSetChanged()
//                    rvAdapter.notifyItemInserted(list.size-1)
                }
            }
    }


    private fun applyQuery() {

        val reqName = binding.editQuery.text.toString()
        userListener?.remove() // Remove existing listener if any

        userListener = db.collection("users")
            .whereEqualTo("Name",reqName)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Handle the error
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    list.clear()
                    for (document in snapshots.documents) {
                        val user = document.toObject(User::class.java)
                        user?.id = document.id
                        if (user != null) {
                            list.add(user)
                        }
                    }

                }
                rvAdapter.notifyDataSetChanged()

            }
    }

    private fun toggleListeners() {

        // Determine which listener to set up based on current state
        if (binding.editQuery.text.isNullOrBlank()) {
            // If query text is blank, revert to setupRealTimeListener
            setupRealTimeListener()
        } else {
            // Otherwise, apply the query
            applyQuery()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userListener?.remove() // Remove the listener when the activity is destroyed
    }


    //toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.homee ->{
                intent = Intent(this@FirestoreActivity,NavDrawerActivity::class.java)
                startActivity(intent)
            }
            R.id.msg ->{
                Toast.makeText(this, "No messages", Toast.LENGTH_SHORT).show()
            }
            R.id.batchedWrite ->{
                changeName("uEIb4DRQTQK9tyhrWIWh", "Po","Dragon Master")
            }
            R.id.transaction ->{
                changeName2("uEIb4DRQTQK9tyhrWIWh")
            }

        }
        return true
    }

    //execute only if both change succeed
    private fun changeName(personId: String,newName :String , newPass : String)
    = CoroutineScope(Dispatchers.IO).launch {
        try {
            db.runBatch{ batch ->
                val personRef = db.collection("users").document(personId)
                batch.update(personRef, "Name", newName)
                batch.update(personRef, "Password", newPass)
            }.await() // Await the completion of the batch operation

        }
        catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@FirestoreActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //allows read and write
    private fun changeName2(personId: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            db.runTransaction(){ transaction ->
                val personRef = db.collection("users").document(personId)
                val person= transaction.get(personRef)
                val newName = "Super" + person["Name"] as String
                val newPass = "Secret" + person["Password"] as String
                transaction.update(personRef, "Name", newName)
                transaction.update(personRef, "Password", newPass)
                null
            }.await()
        }
        catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@FirestoreActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableEdgeToEdge() {
        // Your edge-to-edge implementation
        // This typically involves setting flags or adjusting layout to handle system insets
    }
}