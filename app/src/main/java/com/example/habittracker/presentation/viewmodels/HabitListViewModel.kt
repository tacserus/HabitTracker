package com.example.habittracker.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.database.HabitRepository
import com.example.habittracker.domain.enums.FilterType
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.enums.SortingType
import com.example.habittracker.domain.models.HabitEntity
import kotlinx.coroutines.launch

class HabitListViewModel(
    private val habitRepository: HabitRepository,
    private val application: Application
) : ViewModel() {
    private val currentFilters: MutableMap<FilterType, String> = mutableMapOf()
    private var currentSortingType: SortingType = SortingType.DEFAULT
    private var currentSearchingWord: String = ""

    private var _allHabits: List<HabitEntity> = mutableListOf()
    private val _currentItems = MutableLiveData<List<HabitEntity>>(mutableListOf())
    private val _goodHabits = MutableLiveData<List<HabitEntity>>(mutableListOf())
    private val _badHabits = MutableLiveData<List<HabitEntity>>(mutableListOf())

    val goodHabits: LiveData<List<HabitEntity>> get() = _goodHabits
    val badHabits: LiveData<List<HabitEntity>> get() = _badHabits

    val syncComplete = MutableLiveData(false)

    init {
        habitRepository.getAllHabits().observeForever { habits ->
            _allHabits = habits
            checkOptions()
        }
    }

    fun sync() {
        viewModelScope.launch {
            syncComplete.value = false
            try {
                habitRepository.sync()
                syncComplete.value = true
            } finally {
                syncComplete.value = true
            }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            val habit = habitRepository.getHabitById(id)

            if (habit != null) {
                habitRepository.deleteItem(habit)
            }
        }
    }

    fun saveCompletedDate(id: String, habitType: HabitType, context: Context) {
        viewModelScope.launch {
            val habit = habitRepository.getHabitById(id)

            if (habit != null) {
                if (habitType == HabitType.GoodHabit) {
                    Toast.makeText(context, "You are breathtaking!", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Стоит выполнить еще n раз", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Хватит это делать!", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Можно выполнить еще n раз", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun applyFilters(filters: MutableMap<FilterType, String>) {
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

    private fun applyFilter(filteredHabits: List<HabitEntity>, filterType: FilterType, value: String): List<HabitEntity> {
        val newFilteredItems = when (filterType) {
            FilterType.QUANTITY -> filteredHabits.filter { it.count.toString() == value }
            FilterType.FREQUENCY -> filteredHabits.filter { it.frequency.toString() == value }
        }

        currentFilters[filterType] = value

        return newFilteredItems
    }

    private fun sortByField(sortingField: SortingType) {
        when (sortingField) {
            SortingType.QUANTITY -> _currentItems.value = _allHabits.sortedBy { it.count }
            SortingType.FREQUENCY -> _currentItems.value = _allHabits.sortedBy { it.frequency }
            else -> _currentItems.value = _allHabits.sortedBy { it.title }
        }

        currentSortingType = sortingField
    }

    private fun findByWord(word: String) {
        currentSearchingWord = word
        _currentItems.value = _currentItems.value?.filter { it.title.startsWith(word) || it.title.endsWith(word) } ?: listOf()
    }

    private fun checkOptions() {
        sortByField(currentSortingType)
        applyFilters(currentFilters)

        if (currentSearchingWord.isNotBlank()) {
            findByWord(currentSearchingWord)
        }

        updateHabits()
    }

    fun applyOptions(
        sortingType: SortingType,
        filters: MutableMap<FilterType, String>,
        searchingWord: String
    ) {
        sortByField(sortingType)
        applyFilters(filters)

        if (searchingWord.isNotBlank()) {
            findByWord(searchingWord)
        }

        updateHabits()
    }

    fun reset() {
        currentFilters.clear()
        currentSortingType = SortingType.DEFAULT
        currentSearchingWord = ""
        sortByField(currentSortingType)
        updateHabits()
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

    private fun updateHabits() {
        _goodHabits.value = _currentItems.value?.filter {
            it.type == HabitType.GoodHabit
        }
        _badHabits.value = _currentItems.value?.filter {
            it.type == HabitType.BadHabit
        }
    }
}