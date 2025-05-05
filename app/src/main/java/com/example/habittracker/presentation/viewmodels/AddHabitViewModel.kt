package com.example.habittracker.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.database.HabitsRepository
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.models.AddHabitEvent
import com.example.habittracker.domain.models.AddHabitState
import com.example.habittracker.domain.models.Habit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddHabitViewModel(
    private val habitsRepository: HabitsRepository,
    private val application: Application
) : ViewModel() {
    private val TAG = "add_habit_fragment"
    private val _stateLiveData = MutableLiveData<AddHabitState>()
    private val _events = MutableSharedFlow<AddHabitEvent>()
    val events = _events.asSharedFlow()
    val stateLiveData: LiveData<AddHabitState> get() = _stateLiveData
    var id: String = ""

    fun initState(id: String) {
        this.id = id
        val selectedHabit = habitsRepository.getHabitById(id)

        _stateLiveData.value = AddHabitState(
            id = selectedHabit?.id ?: "",
            title = selectedHabit?.title ?: "",
            description = selectedHabit?.description ?: "",
            priority = selectedHabit?.priority ?: "",
            type = selectedHabit?.type ?: application.getString(HabitType.GoodHabit.id),
            quantity = selectedHabit?.quantity ?: "",
            frequency = selectedHabit?.frequency ?: ""
        )
    }

    fun onSaveClicked() {
        if (id.isNotBlank()) {
            updateHabit()
        } else {
            addHabit()
        }

        onNavigateButtonClicked()
    }

    fun onNavigateButtonClicked() {
        viewModelScope.launch {
            _events.emit(AddHabitEvent.NavigateBack)
        }
    }

    private fun updateHabit() {
        val updatedHabit = getHabitFromState()

        viewModelScope.launch {
            habitsRepository.updateHabit(updatedHabit)
        }
    }

    private fun addHabit() {
        val newHabit = getHabitFromState()

        viewModelScope.launch {
            habitsRepository.addHabit(newHabit)
        }
    }

    private fun getHabitFromState(): Habit {
        val state = _stateLiveData.value

        return Habit(
            id = if (state?.id?.isNotBlank() == true) state.id else Habit.getRandomId(),
            title = state?.title ?: "",
            description = state?.description ?: "",
            type = state?.type ?: "",
            priority = state?.priority ?: "",
            quantity = state?.quantity ?: "",
            frequency = state?.frequency ?: ""
        )
    }

    fun onTitleChanged(newTitle: String) {
        _stateLiveData.value = stateLiveData.value?.copy(title = newTitle)
    }

    fun onDescriptionChanged(newDescription: String) {
        _stateLiveData.value = stateLiveData.value?.copy(description = newDescription)
    }

    fun onPriorityChanged(newPriority: String) {
        _stateLiveData.value = stateLiveData.value?.copy(priority = newPriority)
    }

    fun onTypeChanged(newType: String) {
        _stateLiveData.value = stateLiveData.value?.copy(type = newType)
    }

    fun onQuantityChanged(newQuantity: String) {
        _stateLiveData.value = stateLiveData.value?.copy(quantity = newQuantity)
    }

    fun onFrequencyChanged(newFrequency: String) {
        _stateLiveData.value = stateLiveData.value?.copy(frequency = newFrequency)
    }
}