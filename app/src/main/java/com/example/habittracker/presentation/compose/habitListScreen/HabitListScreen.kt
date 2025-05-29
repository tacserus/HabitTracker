package com.example.habittracker.presentation.compose.habitListScreen

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.models.Habit
import com.example.domain.models.HabitListEvent
import com.example.domain.models.Type
import com.example.habittracker.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    type: Type,
    stateFlow: StateFlow<List<Habit>>,
    events: SharedFlow<HabitListEvent>,
    onSyncHabits: () -> Unit,
    onOpenUpdateHabitFragment: (id: String) -> Unit,
    onCompleteHabit: (id: String) -> Unit,
    onDeleteHabit: (id: String) -> Unit
) {
    val habits by stateFlow.collectAsState(initial = emptyList())

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    val goodLowMessage = stringResource(R.string.good_low_message)
    val goodHighMessage = stringResource(R.string.good_high_message)
    val badLowMessage = stringResource(R.string.bad_low_message)
    val badHighMessage = stringResource(R.string.bad_high_message)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        events.collect { event ->
            val message = when (event) {
                is HabitListEvent.ShowLowToast -> {
                    when (type) {
                        Type.GoodHabit -> String.format(goodLowMessage, event.difference)
                        Type.BadHabit -> String.format(badLowMessage, event.difference)
                    }
                }
                HabitListEvent.ShowHighToast -> {
                    when (type) {
                        Type.GoodHabit -> goodHighMessage
                        Type.BadHabit -> badHighMessage
                    }
                }
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                onSyncHabits()
                isRefreshing = false
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp)
        ) {
            items(
                items = habits,
                key = { habit -> habit.id },
            ) { habit ->
                Habit(
                    habit = habit,
                    onItemClicked = onOpenUpdateHabitFragment,
                    onCompleteClicked = onCompleteHabit,
                    onDeleteHabit = onDeleteHabit
                )
            }
        }
    }
}