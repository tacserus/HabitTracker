package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HabitRepository
import com.example.data.database.HabitEntity
import com.example.data.database.HabitType
import com.example.domain.models.HabitListEvent
import com.example.habittracker.presentation.enums.FilterType
import com.example.habittracker.presentation.enums.SortingType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class HabitListViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {
    private var currentFilters: MutableMap<FilterType, String> = mutableMapOf()
    private var currentSortingType: SortingType = SortingType.DEFAULT
    private var currentSearchingWord: String = ""

    private val _currentItems = MutableStateFlow<List<HabitEntity>>(emptyList())

    private val _goodHabits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val goodHabits: StateFlow<List<HabitEntity>> get() = _goodHabits

    private val _badHabits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val badHabits: StateFlow<List<HabitEntity>> get() = _badHabits

    val syncComplete = MutableStateFlow(false)

    private val _events = MutableSharedFlow<HabitListEvent>()
    val events = _events.asSharedFlow()

    init {
        getAllHabits()
    }

    private fun getAllHabits() {
        viewModelScope.launch {
            habitRepository.getAllHabits().collect { habits ->
                checkOptions(habits)
            }
        }
    }

    private fun checkOptions(habits: List<HabitEntity>) {
        sortByField(habits)
        applyFilters()

        if (currentSearchingWord.isNotBlank()) {
            findByWord(currentSearchingWord)
        }

        updateHabits()
    }

    private fun sortByField(habits: List<HabitEntity>) {
        val sortedItems = when (currentSortingType) {
            SortingType.QUANTITY -> habits.sortedBy { it.count.toInt() }
            SortingType.FREQUENCY -> habits.sortedBy { it.frequency.toInt() }
            else -> habits.sortedBy { it.title }
        }
        _currentItems.value = sortedItems
    }

    private fun applyFilters() {
        val filteredItems = currentFilters.entries.fold(_currentItems.value) { currentList, filterEntry ->
            applyFilter(currentList, filterEntry.key, filterEntry.value)
        }
        _currentItems.value = filteredItems
    }

    private fun applyFilter(filteredHabits: List<HabitEntity>, filterType: FilterType, value: String): List<HabitEntity>  {
        return when (filterType) {
            FilterType.QUANTITY -> filteredHabits.filter { it.count == value }
            FilterType.FREQUENCY -> filteredHabits.filter { it.frequency == value }
        }.also { currentFilters[filterType] = value }
    }

    private fun findByWord(word: String) {
        val filteredByWord = _currentItems.value.filter { it.title.startsWith(word) || it.title.endsWith(word) }
        _currentItems.value = filteredByWord
    }

    private fun updateHabits() {
        _goodHabits.value = _currentItems.value.filter {
            it.type == HabitType.GoodHabit
        }
        _badHabits.value = _currentItems.value.filter {
            it.type == HabitType.BadHabit
        }
    }

    fun getCurrentFilters(): MutableMap<FilterType, String> {
        return currentFilters
    }

    fun getCurrentSortingType(): SortingType {
        return currentSortingType
    }

    fun getCurrentSearchingWord(): String {
        return currentSearchingWord
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

    fun saveDoneMark(id: String) {
        viewModelScope.launch {
            habitRepository.updateDoneMark(id)

            val habit = habitRepository.getHabitById(id)

            if (habit != null) {
                val difference = habit.doneMarks.size - habit.count.toInt()

                if (difference >= 0) {
                    _events.emit(HabitListEvent.ShowHighToast)
                } else {
                    _events.emit(HabitListEvent.ShowLowToast(difference.absoluteValue))
                }
            }

        }
    }

    fun applyOptions(
        selectedSortingType: SortingType,
        filters: MutableMap<FilterType, String>,
        searchingWord: String
    ) {
        currentSortingType = selectedSortingType
        currentFilters = filters
        currentSearchingWord = searchingWord

        viewModelScope.launch {
            checkOptions(habitRepository.getListAllHabits())
        }
    }

    fun resetOptions() {
        currentSortingType = SortingType.DEFAULT
        currentFilters = mutableMapOf()
        currentSearchingWord = ""

        viewModelScope.launch {
            checkOptions(habitRepository.getListAllHabits())
        }
    }
}