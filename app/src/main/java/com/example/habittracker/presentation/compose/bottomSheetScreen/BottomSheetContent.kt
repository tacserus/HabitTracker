package com.example.habittracker.presentation.compose.bottomSheetScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittracker.R
import com.example.habittracker.presentation.viewmodels.HabitListViewModel

@Composable
fun BottomSheetContent(
    habitListViewModel: HabitListViewModel,
    onDismiss: () -> Unit,
    onApplyOptions: () -> Unit,
    onResetOptions: () -> Unit
) {
    val uiState by habitListViewModel.stateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.search),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = uiState.searchText,
            onValueChange = { habitListViewModel.onSearchTextChange(it) },
            label = { Text(stringResource(R.string.search_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.filters),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.habit_count))
        OutlinedTextField(
            value = uiState.countFilter,
            onValueChange = {
                if (it.matches(Regex("^\\d*\$"))) {
                    habitListViewModel.onQuantityFilterChange(it)
                }
            },
            label = { Text(stringResource(R.string.zero)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.habit_frequency))
        OutlinedTextField(
            value = uiState.frequencyFilter,
            onValueChange = {
                if (it.matches(Regex("^\\d*\$"))) {
                    habitListViewModel.onFrequencyFilterChange(it)
                }
            },
            label = { Text(stringResource(R.string.zero)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.sorting),
            style = MaterialTheme.typography.titleMedium
        )

        SortRadioGroup(
            selectedSortOption = uiState.selectedSortOption,
            onSortOptionSelected = { habitListViewModel.onSortOptionSelected(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                onApplyOptions()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.habit_apply))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onResetOptions()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.habit_reset))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.habit_cancel))
        }
    }
}