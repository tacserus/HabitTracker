package com.example.habittracker.presentation.viewmodels

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.HabitListEvent
import com.example.domain.models.HabitModel
import com.example.domain.models.Type
import com.example.domain.usecases.AddDoneMarkUseCase
import com.example.domain.usecases.DeleteHabitUseCase
import com.example.domain.usecases.GetAllHabitsUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.GetListAllHabitsUseCase
import com.example.domain.usecases.SyncHabitsUseCase
import com.example.habittracker.presentation.enums.FilterType
import com.example.habittracker.presentation.enums.SortingType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.absoluteValue

class HabitListViewModel(
    val getAllHabitsUseCase: GetAllHabitsUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase,
    val addDoneMarkUseCase: AddDoneMarkUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase,
    val syncHabitsUseCase: SyncHabitsUseCase,
    val getListAllHabitsUseCase: GetListAllHabitsUseCase
) : ViewModel() {
    private var currentFilters: MutableMap<FilterType, String> = mutableMapOf()
    private var currentSortingType: SortingType = SortingType.DEFAULT
    private var currentSearchingWord: String = ""

    private val _currentItems = MutableStateFlow<List<HabitModel>>(emptyList())

    private val _goodHabits = MutableStateFlow<List<HabitModel>>(emptyList())
    val goodHabits: StateFlow<List<HabitModel>> get() = _goodHabits

    private val _badHabits = MutableStateFlow<List<HabitModel>>(emptyList())
    val badHabits: StateFlow<List<HabitModel>> get() = _badHabits

    val syncComplete = MutableStateFlow(false)

    private val _events = MutableSharedFlow<HabitListEvent>()
    val events = _events.asSharedFlow()

    init {
        getAllHabits()
    }

    private fun getAllHabits() {
        viewModelScope.launch {
            getAllHabitsUseCase.execute().collect { habits ->
                checkOptions(habits)
            }
        }
    }

    private fun checkOptions(habits: List<HabitModel>) {
        sortByField(habits)
        applyFilters()

        if (currentSearchingWord.isNotBlank()) {
            findByWord(currentSearchingWord)
        }

        updateHabits()
    }

    private fun sortByField(habits: List<HabitModel>) {
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

    private fun applyFilter(filteredHabits: List<HabitModel>, filterType: FilterType, value: String): List<HabitModel>  {
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
            it.type == Type.GoodHabit
        }
        _badHabits.value = _currentItems.value.filter {
            it.type == Type.BadHabit
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
                syncHabitsUseCase.execute()
                syncComplete.value = true
            } finally {
                syncComplete.value = true
            }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            getHabitByIdUseCase.execute(id)?.let { habit ->
                deleteHabitUseCase.execute(habit)
            }
        }
    }

    fun saveDoneMark(id: String) {
        viewModelScope.launch {
            val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond
            } else {
                System.currentTimeMillis()
            }

            val updatedHabit = getHabitByIdUseCase.execute(id)

            if (updatedHabit != null) {
                addDoneMarkUseCase.execute(updatedHabit, date)

                val habit = getHabitByIdUseCase.execute(id)

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
            checkOptions(getListAllHabitsUseCase.execute())
        }
    }

    fun resetOptions() {
        currentSortingType = SortingType.DEFAULT
        currentFilters = mutableMapOf()
        currentSearchingWord = ""

        viewModelScope.launch {
            checkOptions(getListAllHabitsUseCase.execute())
        }
    }
}