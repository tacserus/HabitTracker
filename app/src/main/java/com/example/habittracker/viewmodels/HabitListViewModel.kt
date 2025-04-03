package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.database.HabitsRepository
import com.example.habittracker.enums.FilterType
import com.example.habittracker.enums.SortingType
import com.example.habittracker.models.Habit

class HabitListViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {
    private val currentFilters: MutableMap<FilterType, String> = mutableMapOf()
    private var currentSortingType: SortingType = SortingType.DEFAULT
    private var currentSearchingWord: String = ""

    private var _allHabits: List<Habit> = mutableListOf()
    private val _currentItems = MutableLiveData<List<Habit>>(mutableListOf())

    val items: LiveData<List<Habit>> get() = _currentItems

    init {
        habitsRepository.getAllHabits().observeForever { habits ->
            _allHabits = habits
            checkOptions()
        }

    }

    fun deleteHabit(habit: Habit) {
        habitsRepository.deleteHabit(habit)
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

    private fun applyFilter(filteredHabits: List<Habit>, filterType: FilterType, value: String): List<Habit> {
        val newFilteredItems = when (filterType) {
            FilterType.QUANTITY -> filteredHabits.filter { it.quantity == value }
            FilterType.FREQUENCY -> filteredHabits.filter { it.frequency == value }
        }

        currentFilters[filterType] = value

        return newFilteredItems
    }

    fun sortByField(sortingField: SortingType) {
        when (sortingField) {
            SortingType.QUANTITY -> _currentItems.value = _allHabits.sortedBy { it.quantity }
            SortingType.FREQUENCY -> _currentItems.value = _allHabits.sortedBy { it.frequency }
            else -> _currentItems.value = _allHabits.sortedBy { it.title }
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