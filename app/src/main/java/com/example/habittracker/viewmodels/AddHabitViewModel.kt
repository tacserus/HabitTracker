package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.database.HabitsRepository
import com.example.habittracker.enums.HabitType
import com.example.habittracker.models.AddHabitState
import com.example.habittracker.models.Habit

class AddHabitViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {
    private val _stateLiveData = MutableLiveData<AddHabitState>()
    val stateLiveData: LiveData<AddHabitState> get() = _stateLiveData

    init {
        _stateLiveData.value = AddHabitState(
            id = null,
            title = "",
            description = "",
            priority = "",
            type = HabitType.GOODHABIT.description,
            quantity = "",
            frequency = ""
        )
    }

    fun fillStateForEditMode(id: String) {
        if (id == "null")
            return

        val habit = habitsRepository.getHabitById(id)

        _stateLiveData.value = AddHabitState(
            id = habit?.id,
            title = habit?.title ?: "",
            description = habit?.description ?: "",
            priority = habit?.priority ?: "",
            type = habit?.type ?: HabitType.GOODHABIT.description,
            quantity = habit?.quantity ?: "",
            frequency = habit?.frequency ?: ""
        )
    }

    fun updateHabit() {
        habitsRepository.updateHabit(getHabit())
    }

    fun addHabit() {
        habitsRepository.addHabit(getHabit())
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

    private fun getHabit(): Habit {
        return Habit(
            id = _stateLiveData.value!!.id,
            title = _stateLiveData.value?.title ?: "",
            description = _stateLiveData.value?.description ?: "",
            priority = _stateLiveData.value?.priority ?: "",
            type = _stateLiveData.value?.type ?: "",
            quantity = _stateLiveData.value?.quantity ?: "",
            frequency = _stateLiveData.value?.frequency ?: ""
        )
    }

    fun clearState() {
        _stateLiveData.value = AddHabitState(
            id = null,
            title = "",
            description = "",
            priority = "",
            type = "",
            quantity = "",
            frequency = ""
        )
    }
}