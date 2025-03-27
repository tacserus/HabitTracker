package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.models.Item

class AddHabitViewModel : ViewModel() {
    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> get() = _item

    fun createItem(
        id: String?,
        title: String,
        description: String,
        priority: String,
        type: String,
        quantity: String,
        frequency: String
    ) {
        val newItem = Item(
            id = id ?: Item.generateId().toString(),
            title = title,
            description = description,
            priority = priority,
            type = type,
            quantity = quantity,
            frequency = frequency
        )
        _item.value = newItem
    }

    fun fillItem(item: Item) {
        _item.value = item
    }

    fun clearItem() {
        _item.value = Item(
            id = "",
            title = "",
            description = "",
            priority = "",
            type = "",
            quantity = "",
            frequency = ""
        )
    }
}