package com.example.habittracker.presentation.compose.addHabitScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.models.Priority
import com.example.domain.models.Type
import com.example.habittracker.R
import com.example.habittracker.presentation.viewmodels.AddHabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    addHabitViewModel: AddHabitViewModel,
    habitId: String?,
    onNavigateBack: () -> Unit
) {
    val state by addHabitViewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (habitId == null) stringResource(R.string.add) else stringResource(R.string.edit_habit)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(stringResource(R.string.habit_name), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                state?.let {
                    OutlinedTextField(
                        value = it.title,
                        onValueChange = { addHabitViewModel.onTitleChanged(it) },
                        label = { Text(stringResource(R.string.habit_edit_name)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.habit_edit_description), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                state?.let {
                    OutlinedTextField(
                        value = it.description,
                        onValueChange = { value -> addHabitViewModel.onDescriptionChanged(value) },
                        label = { Text(stringResource(R.string.habit_edit_description)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.habit_priority), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                state?.let {
                    PrioritySpinner(
                        selectedPriority = it.priority,
                        onPrioritySelected = { priority -> addHabitViewModel.onPriorityChanged(priority) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.habit_type), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                state?.let {
                    HabitTypeRadioGroup(
                        selectedType = it.type,
                        onTypeSelected = { type -> addHabitViewModel.onTypeChanged(type) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.habit_count), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                state?.let {
                    OutlinedTextField(
                        value = it.count,
                        onValueChange = { value ->
                            if (value.matches(Regex("^\\d*\$"))) {
                                addHabitViewModel.onCountChanged(value)
                            }
                        },
                        label = { Text(stringResource(R.string.habit_edit_quantity)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.habit_frequency), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                state?.let {
                    OutlinedTextField(
                        value = it.frequency,
                        onValueChange = { value ->
                            if (value.matches(Regex("^\\d*\$"))) {
                                addHabitViewModel.onFrequencyChanged(value)
                            }
                        },
                        label = { Text(stringResource(R.string.habit_edit_frequency)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 15.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val priority = Priority.entries.find {
                                it.description == addHabitViewModel.stateFlow.value?.priority
                            } ?: Priority.Lite
                            val type = Type.entries.find {
                                it.description == addHabitViewModel.stateFlow.value?.type
                            } ?: Type.GoodHabit
                            if (addHabitViewModel.checkState()) {
                                addHabitViewModel.onSaveClicked(priority, type)
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 5.dp)
                    ) {
                        Text(stringResource(R.string.habit_save))
                    }

                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(stringResource(R.string.habit_cancel))
                    }
                }
            }
        }
    )
}