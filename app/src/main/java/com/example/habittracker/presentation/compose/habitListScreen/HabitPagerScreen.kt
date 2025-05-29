package com.example.habittracker.presentation.compose.habitListScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.models.Type
import com.example.habittracker.R
import com.example.habittracker.presentation.compose.bottomSheetScreen.BottomSheetContent
import com.example.habittracker.presentation.viewmodels.HabitListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsPagerScreen(
    habitListViewModel: HabitListViewModel,
    onOpenUpdateHabitScreen: (id: String) -> Unit,
    onCompleteHabit: (id: String) -> Unit,
    onDeleteHabit: (id: String) -> Unit,
    onOpenAddHabitScreen: () -> Unit
) {
    val habitTypes = remember { listOf(Type.GoodHabit, Type.BadHabit) }

    val pagerState = rememberPagerState(pageCount = { habitTypes.size })

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showBottomSheet = true }
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.open_menu))
                }
                ExtendedFloatingActionButton(
                    onClick = onOpenAddHabitScreen,
                    Modifier.testTag("addHabit")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_habit))
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    habitTypes.forEachIndexed { index, type ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = when (type) {
                                        Type.GoodHabit -> stringResource(R.string.good_habits)
                                        Type.BadHabit -> stringResource(R.string.bad_habits)
                                    }
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val stateFlow = when (habitTypes[page]) {
                        Type.GoodHabit -> habitListViewModel.goodHabits
                        Type.BadHabit -> habitListViewModel.badHabits
                    }

                    HabitListScreen(
                        habitTypes[page],
                        stateFlow = stateFlow,
                        events = habitListViewModel.events,
                        onSyncHabits = { habitListViewModel.sync() },
                        onOpenUpdateHabitFragment = onOpenUpdateHabitScreen,
                        onCompleteHabit = onCompleteHabit,
                        onDeleteHabit = onDeleteHabit
                    )
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BottomSheetContent(
                        habitListViewModel,
                        onDismiss = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        onApplyOptions = { habitListViewModel.applyOptions() },
                        onResetOptions = { habitListViewModel.resetOptions() }
                    )
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    )
}
