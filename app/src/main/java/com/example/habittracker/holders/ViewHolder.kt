package com.example.habittracker.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ItemLayoutBinding
import com.example.habittracker.models.Item

class ViewHolder(
    private val binding: ItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Item) {
        binding.item = item

        binding.executePendingBindings()
    }
}