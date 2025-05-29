package com.example.habittracker.presentation.viewmodels

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Habit
import com.example.domain.models.HabitListEvent
import com.example.domain.models.Type
import com.example.domain.usecases.AddDoneMarkUseCase
import com.example.domain.usecases.DeleteHabitUseCase
import com.example.domain.usecases.GetAllHabitsUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.GetListAllHabitsUseCase
import com.example.domain.usecases.SyncHabitsUseCase
import com.example.habittracker.presentation.enums.SortingType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.absoluteValue

class HabitListViewModel(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val addDoneMarkUseCase: AddDoneMarkUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val syncHabitsUseCase: SyncHabitsUseCase,
    private val getListAllHabitsUseCase: GetListAllHabitsUseCase
) : ViewModel() {
    private val _currentItems = MutableStateFlow<List<Habit>>(emptyList())

    private val _goodHabits = MutableStateFlow<List<Habit>>(emptyList())
    val goodHabits: StateFlow<List<Habit>> get() = _goodHabits

    private val _badHabits = MutableStateFlow<List<Habit>>(emptyList())
    val badHabits: StateFlow<List<Habit>> get() = _badHabits

    private val _events = MutableSharedFlow<HabitListEvent>()
    val events = _events.asSharedFlow()

    private val _stateFlow = MutableStateFlow(BottomSheetState())
    val stateFlow: StateFlow<BottomSheetState> = _stateFlow.asStateFlow()

    init {
        getAllHabits()
    }

    fun onSearchTextChange(newText: String) {
        _stateFlow.update { it.copy(searchText = newText) }
    }

    fun onQuantityFilterChange(newQuantity: String) {
        _stateFlow.update { it.copy(countFilter = newQuantity) }
    }

    fun onFrequencyFilterChange(newFrequency: String) {
        _stateFlow.update { it.copy(frequencyFilter = newFrequency) }
    }

    fun onSortOptionSelected(option: SortingType) {
        _stateFlow.update { it.copy(selectedSortOption = option) }
    }

    private fun getAllHabits() {
        viewModelScope.launch {
            getAllHabitsUseCase.execute().collect { habits ->
                checkOptions(habits)
            }
        }
    }

    private fun checkOptions(habits: List<Habit>) {
        sortByField(habits)
        applyFilters()

        if (_stateFlow.value.searchText.isNotBlank()) {
            findByWord(_stateFlow.value.searchText)
        }

        updateHabits()
    }

    private fun sortByField(habits: List<Habit>) {
        val sortedItems = when (_stateFlow.value.selectedSortOption) {
            SortingType.COUNT -> habits.sortedBy { it.count.toInt() }
            SortingType.FREQUENCY -> habits.sortedBy { it.frequency.toInt() }
            else -> habits.sortedBy { it.title }
        }
        _currentItems.value = sortedItems
    }

    private fun applyFilters() {
        if (_stateFlow.value.countFilter.isNotBlank()) {
            _currentItems.value = _currentItems.value.filter { it.count == _stateFlow.value.countFilter }
        }
        if (_stateFlow.value.frequencyFilter.isNotBlank()) {
            _currentItems.value = _currentItems.value.filter { it.frequency == _stateFlow.value.frequencyFilter }
        }
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

    fun sync() {
        viewModelScope.launch {
            syncHabitsUseCase.execute()
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
    ) {
        viewModelScope.launch {
            checkOptions(getListAllHabitsUseCase.execute())
        }
    }

    fun resetOptions() {
        _stateFlow.value = BottomSheetState(
            searchText = "",
            countFilter = "",
            frequencyFilter = "",
            selectedSortOption = SortingType.DEFAULT
        )

        viewModelScope.launch {
            checkOptions(getListAllHabitsUseCase.execute())
        }
    }
}