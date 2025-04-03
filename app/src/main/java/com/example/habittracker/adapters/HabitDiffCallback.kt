package com.example.habittracker.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.habittracker.models.Habit

class HabitDiffCallback (
    private val oldList: List<Habit>,
    private val newList: List<Habit>
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