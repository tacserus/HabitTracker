package com.example.habittracker.presentation.compose.habitListScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.models.Habit
import com.example.habittracker.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Habit(
    habit: Habit,
    onItemClicked: (id: String) -> Unit,
    onCompleteClicked: (id: String) -> Unit,
    onDeleteHabit: (id: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onItemClicked(habit.id)
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = habit.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (habit.description.isNotBlank()) {
                Text(
                    text = habit.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            HabitField(label = stringResource(R.string.habit_priority_field), value = habit.priority.description)

            val format = stringResource(R.string.format)
            val formattedDate = remember(habit.date) {
                SimpleDateFormat(format, Locale.getDefault()).format(Date(habit.date))
            }

            HabitField(label = stringResource(R.string.habit_data_field), value = formattedDate)
            Spacer(modifier = Modifier.height(12.dp))

            HabitField(label = stringResource(R.string.habit_count_field), value = habit.count)
            HabitField(label = stringResource(R.string.habit_frequency_field), value = habit.frequency)
            Spacer(modifier = Modifier.height(12.dp))

            HabitField(label = stringResource(R.string.habit_complete_count_field), value = habit.doneMarks.size.toString())

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onDeleteHabit(habit.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete_habit))
            }
            Button(
                onClick = { onCompleteClicked(habit.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.make_done_mark))
            }
        }
    }
}