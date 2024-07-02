package com.example.signup

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.signup.databinding.ItemViewPagerBinding
import com.example.signup.databinding.RvItemBinding

class vpAdapter(var images : List<Int>, var context : Context) :
    RecyclerView.Adapter<vpAdapter.vpViewHolder>(){

    inner class vpViewHolder(var binding: ItemViewPagerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vpViewHolder {

        val binding = ItemViewPagerBinding.inflate(LayoutInflater.from(context),parent,false)
        return vpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: vpViewHolder, position: Int) {
        val curImage = images[position]
        holder.binding.ivimage.setImageResource(curImage)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}