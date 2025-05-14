package com.example.habittracker.presentation.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.database.HabitRepository
import com.example.habittracker.domain.enums.FilterType
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.enums.SortingType
import com.example.habittracker.domain.models.HabitEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HabitListViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {
    private val currentFilters: MutableMap<FilterType, String> = mutableMapOf()
    private var currentSortingType: SortingType = SortingType.DEFAULT
    private var currentSearchingWord: String = ""

    private val _currentItems = MutableStateFlow<List<HabitEntity>>(emptyList())
    val currentItems: StateFlow<List<HabitEntity>> get() = _currentItems

    private val _goodHabits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val goodHabits: StateFlow<List<HabitEntity>> get() = _goodHabits

    private val _badHabits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val badHabits: StateFlow<List<HabitEntity>> get() = _badHabits

    val syncComplete = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            habitRepository.getAllHabits().collect { habits ->
                checkOptions(habits)
            }
        }
    }

    fun sync() {
        viewModelScope.launch {
            syncComplete.value = false
            try {
                habitRepository.syncHabits()
                syncComplete.value = true
            } finally {
                syncComplete.value = true
            }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            habitRepository.getHabitById(id)?.let { habit ->
                habitRepository.deleteHabit(habit)
            }
        }
    }

    fun saveCompletedDate(id: String, habitType: HabitType, context: Context) {
        viewModelScope.launch {
            habitRepository.getHabitById(id)?.let { habit ->
                showToast(habitType, context)
            }
        }
    }

    private fun showToast(habitType: HabitType, context: Context) {
        val message = when (habitType) {
            HabitType.GoodHabit -> listOf("You are breathtaking!", "Стоит выполнить еще n раз")
            HabitType.BadHabit -> listOf("Хватит это делать!", "Можно выполнить еще n раз")
        }
        message.forEach { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    private fun applyFilters(habits: List<HabitEntity>) {
        val filteredItems = currentFilters.entries.fold(habits) { currentList, filterEntry ->
            applyFilter(currentList, filterEntry.key, filterEntry.value)
        }
        _currentItems.value = filteredItems
    }

    private fun applyFilter(filteredHabits: List<HabitEntity>, filterType: FilterType, value: String): List<HabitEntity> {
        return when (filterType) {
            FilterType.QUANTITY -> filteredHabits.filter { it.count == value }
            FilterType.FREQUENCY -> filteredHabits.filter { it.frequency == value }
        }.also { currentFilters[filterType] = value }
    }

    private fun sortByField(habits: List<HabitEntity>) {
        val sortedItems = when (currentSortingType) {
            SortingType.QUANTITY -> habits.sortedBy { it.count }
            SortingType.FREQUENCY -> habits.sortedBy { it.frequency }
            else -> habits.sortedBy { it.title }
        }
        _currentItems.value = sortedItems
    }

    private fun findByWord(word: String) {
        val filteredByWord = _currentItems.value.filter { it.title.startsWith(word) || it.title.endsWith(word) }
        _currentItems.value = filteredByWord
    }

    private fun checkOptions(habits: List<HabitEntity>) {
        sortByField(habits)
        applyFilters(habits)

        if (currentSearchingWord.isNotBlank()) {
            findByWord(currentSearchingWord)
        }

        updateHabits()
    }

    private fun updateHabits() {
        _goodHabits.value = _currentItems.value.filter {
            it.type == HabitType.GoodHabit
        }
        _badHabits.value = _currentItems.value.filter {
            it.type == HabitType.BadHabit
        }
    }
}