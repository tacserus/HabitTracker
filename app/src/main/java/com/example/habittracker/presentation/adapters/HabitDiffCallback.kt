package com.example.habittracker.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.data.database.HabitEntity

class HabitDiffCallback (
    private val oldList: List<HabitEntity>,
    private val newList: List<HabitEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}