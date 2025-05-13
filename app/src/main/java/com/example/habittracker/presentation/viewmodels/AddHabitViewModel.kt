package com.example.habittracker.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.database.HabitRepository
import com.example.habittracker.domain.HabitMapper
import com.example.habittracker.domain.enums.HabitStatus
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.enums.Priority
import com.example.habittracker.domain.models.AddHabitEvent
import com.example.habittracker.domain.models.AddHabitState
import com.example.habittracker.domain.models.HabitEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddHabitViewModel(
    private val habitRepository: HabitRepository,
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

        viewModelScope.launch {
            val selectedHabit = habitRepository.getHabitById(id)

            if (selectedHabit != null) {
                val selectedHabitState = HabitMapper.INSTANCE.entityToState(selectedHabit, application)

                _stateLiveData.value = selectedHabitState
            } else {
                _stateLiveData.value = AddHabitState(
                    id = UUID.randomUUID().toString(),
                    apiId = null,
                    title = "",
                    description = "",
                    priority = application.getString(Priority.Lite.id),
                    type = application.getString(HabitType.GoodHabit.id),
                    count = "",
                    frequency = "",
                    habitStatus = HabitStatus.ADD
                )
            }
        }
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
            habitRepository.updateHabit(updatedHabit)
        }
    }

    private fun addHabit() {
        val newHabit = getHabitFromState()

        viewModelScope.launch {
            habitRepository.addHabit(newHabit)
        }
    }

    private fun getHabitFromState(): HabitEntity {
        val state = _stateLiveData.value

        return HabitMapper.INSTANCE.stateToEntity(state!!, application)
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

    fun onCountChanged(newCount: String) {
        _stateLiveData.value = stateLiveData.value?.copy(count = newCount)
    }

    fun onFrequencyChanged(newFrequency: String) {
        _stateLiveData.value = stateLiveData.value?.copy(frequency = newFrequency)
    }
}