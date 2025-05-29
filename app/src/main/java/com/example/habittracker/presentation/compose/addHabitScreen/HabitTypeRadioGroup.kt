package com.example.habittracker.presentation.compose.addHabitScreen

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
import com.example.domain.models.Type
import com.example.habittracker.R

@Composable
fun HabitTypeRadioGroup(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (selectedType == Type.GoodHabit.description),
                    onClick = { onTypeSelected(Type.GoodHabit.description) },
                    role = Role.RadioButton
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (selectedType == Type.GoodHabit.description),
                onClick = null
            )
            Text(
                text = stringResource(R.string.habit_good_type),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (selectedType == Type.BadHabit.description),
                    onClick = { onTypeSelected(Type.BadHabit.description) },
                    role = Role.RadioButton
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (selectedType == Type.BadHabit.description),
                onClick = null
            )
            Text(
                text = stringResource(R.string.habit_bad_type),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}