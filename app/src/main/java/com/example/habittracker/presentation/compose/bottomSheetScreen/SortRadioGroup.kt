package com.example.habittracker.presentation.compose.bottomSheetScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.habittracker.R
import com.example.habittracker.presentation.enums.SortingType

@Composable
fun SortRadioGroup(
    selectedSortOption: SortingType,
    onSortOptionSelected: (SortingType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val sortOptions = listOf(
            Pair(SortingType.DEFAULT, stringResource(R.string.habit_name)),
            Pair(SortingType.COUNT, stringResource(R.string.habit_count)),
            Pair(SortingType.FREQUENCY, stringResource(R.string.habit_frequency))
        )

        sortOptions.forEach { (option, text) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedSortOption == option),
                        onClick = { onSortOptionSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedSortOption == option),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}