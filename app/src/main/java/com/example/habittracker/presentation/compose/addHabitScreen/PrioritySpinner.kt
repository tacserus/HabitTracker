package com.example.habittracker.presentation.compose.addHabitScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.domain.models.Priority
import com.example.habittracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySpinner(
    selectedPriority: String,
    onPrioritySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val priorities = remember { Priority.entries }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedPriority,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.habit_priority)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true).fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            priorities.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.description) },
                    onClick = {
                        onPrioritySelected(priority.description)
                        expanded = false
                    }
                )
            }
        }
    }
}