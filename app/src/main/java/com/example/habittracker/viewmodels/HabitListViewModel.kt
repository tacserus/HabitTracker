package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.enums.FilterType
import com.example.habittracker.enums.SortingType
import com.example.habittracker.models.Item

class HabitListViewModel : ViewModel() {
    private val currentFilters: MutableMap<FilterType, String> = mutableMapOf()
    private var currentSortingType: SortingType = SortingType.DEFAULT
    private var currentSearchingWord: String = ""

    private val _allItems: MutableList<Item> = mutableListOf()
    private val _currentItems = MutableLiveData<List<Item>>(mutableListOf())

    val items: LiveData<List<Item>> get() = _currentItems

    fun getItemById(id: String): Item? {
        return _allItems.find { it.id == id }
    }

    fun addItem(item: Item) {
        _allItems.add(item)

        checkOptions()
    }

    fun updateItem(item: Item) {
        val position = _allItems.indexOfFirst { it.id == item.id }
        if (position != -1) {
            _allItems[position] = item
        }

        checkOptions()
    }

    fun applyFilters(filters: MutableMap<FilterType, String>) {
        var filteredItems = _currentItems.value ?: listOf()

        for (currentFilterType in filters.keys) {
            filteredItems = applyFilter(
                filteredItems,
                currentFilterType,
                filters[currentFilterType] ?: ""
            )
        }

        _currentItems.value = filteredItems
    }

    private fun applyFilter(filteredItems: List<Item>, filterType: FilterType, value: String): List<Item> {
        val newFilteredItems = when (filterType) {
            FilterType.QUANTITY -> filteredItems.filter { it.quantity == value }
            FilterType.FREQUENCY -> filteredItems.filter { it.frequency == value }
        }

        currentFilters[filterType] = value

        return newFilteredItems
    }

    fun sortByField(sortingField: SortingType) {
        when (sortingField) {
            SortingType.QUANTITY -> _currentItems.value = _allItems.sortedBy { it.quantity }
            SortingType.FREQUENCY -> _currentItems.value = _allItems.sortedBy { it.frequency }
            else -> _currentItems.value = _allItems.sortedBy { it.title }
        }

        currentSortingType = sortingField
    }

    fun findByWord(word: String) {
        currentSearchingWord = word
        _currentItems.value = _currentItems.value?.filter { it.title.startsWith(word) || it.title.endsWith(word) } ?: listOf()
    }

    private fun checkOptions() {
        sortByField(currentSortingType)
        applyFilters(currentFilters)

        if (currentSearchingWord.isNotBlank()) {
            findByWord(currentSearchingWord)
        }
    }

    fun reset() {
        currentFilters.clear()
        currentSortingType = SortingType.DEFAULT
        sortByField(currentSortingType)
        currentSearchingWord = ""
    }

    fun getCurrentFilters(): Map<FilterType, String> {
        return currentFilters
    }

    fun getCurrentSortingType(): SortingType {
        return currentSortingType
    }

    fun getCurrentSearchingWord(): String {
        return currentSearchingWord
    }
}