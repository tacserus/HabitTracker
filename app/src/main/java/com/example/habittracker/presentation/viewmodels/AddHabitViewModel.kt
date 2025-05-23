package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.mapper.HabitMapper
import com.example.domain.models.AddHabitEvent
import com.example.domain.models.AddHabitState
import com.example.domain.models.HabitModel
import com.example.domain.models.HabitStatus
import com.example.domain.models.Priority
import com.example.domain.models.Type
import com.example.domain.usecases.AddHabitUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.UpdateHabitUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddHabitViewModel(
    val addHabitUseCase: AddHabitUseCase,
    val updateHabitUseCase: UpdateHabitUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<AddHabitState?>(null)
    val stateFlow: StateFlow<AddHabitState?> get() = _stateFlow

    private val _events = MutableSharedFlow<AddHabitEvent>()
    val events = _events.asSharedFlow()
    var id: String = ""

    fun initState(id: String, defaultPriority: String, defaultType: String) {
        this.id = id

        viewModelScope.launch {
            val selectedHabit = getHabitByIdUseCase.execute(id)

            val selectedHabitState = if (selectedHabit != null) {
                HabitMapper.INSTANCE.modelToState(selectedHabit, defaultPriority, defaultType)
            } else {
                AddHabitState(
                    id = UUID.randomUUID().toString(),
                    apiId = null,
                    title = "",
                    description = "",
                    priority = defaultPriority,
                    type = defaultType,
                    count = "",
                    frequency = "",
                    habitStatus = HabitStatus.ADD,
                    doneMarks = listOf(),
                    isDoneMarksSynced = false
                )
            }

            _stateFlow.value = selectedHabitState
        }
    }

    fun onSaveClicked(priority: Priority, type: Type) {
        val newHabit = getHabitFromState(priority, type)

        viewModelScope.launch {
            if (id.isNotBlank()) {
                updateHabitUseCase.execute(newHabit)
            } else {
                addHabitUseCase.execute(newHabit)
            }

            onNavigateButtonClicked()
        }
    }

    fun onNavigateButtonClicked() {
        viewModelScope.launch {
            _events.emit(AddHabitEvent.NavigateBack)
        }
    }

    private fun getHabitFromState(priority: Priority, type: Type): HabitModel {
        val state = _stateFlow.value ?: throw IllegalStateException("State should not be null")
        return HabitMapper.INSTANCE.stateToModel(state, priority, type)
    }

    fun onTitleChanged(newTitle: String) {
        _stateFlow.value = _stateFlow.value?.copy(title = newTitle)
    }

    fun onDescriptionChanged(newDescription: String) {
        _stateFlow.value = _stateFlow.value?.copy(description = newDescription)
    }

    fun onPriorityChanged(newPriority: String) {
        _stateFlow.value = _stateFlow.value?.copy(priority = newPriority)
    }

    fun onTypeChanged(newType: String) {
        _stateFlow.value = _stateFlow.value?.copy(type = newType)
    }

    fun onCountChanged(newCount: String) {
        _stateFlow.value = _stateFlow.value?.copy(count = newCount)
    }

    fun onFrequencyChanged(newFrequency: String) {
        _stateFlow.value = _stateFlow.value?.copy(frequency = newFrequency)
    }
}