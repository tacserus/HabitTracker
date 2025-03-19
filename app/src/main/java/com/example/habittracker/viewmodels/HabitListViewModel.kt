package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.models.Item

class HabitListViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>(mutableListOf())
    val items: LiveData<List<Item>> get() = _items

    fun addItem(item: Item) {
        val newItems = _items.value?.toMutableList() ?: mutableListOf()
        newItems.add(item)
        _items.value = newItems
    }

    fun updateItem(item: Item) {
        val newItems = _items.value?.toMutableList() ?: return

        val position = newItems.indexOfFirst { it.id == item.id }
        if (position != -1) {
            newItems[position] = item
            _items.value = newItems
        }
    }
}