package com.example.signup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.signup.databinding.RvItemBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.collections.MutableList


class RvAdapter(var context: Context, var list : ArrayList<User>) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = RvItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.name.text = list[position].Name
        holder.binding.password.text = list[position].Password

        holder.binding.delete.setOnClickListener{
            val db = Firebase.firestore
            db.collection("users").document(list[position].id!!).delete().addOnSuccessListener {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()

                list.removeAt(position)
                notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
        holder.binding.update.setOnClickListener {
            val intent = Intent(context,UpdateActivity::class.java)
            intent.putExtra("NAME",list[position].Name)
            intent.putExtra("PASS",list[position].Password)
            intent.putExtra("ID",list[position].id)
            context.startActivity(intent)
        }
    }

}