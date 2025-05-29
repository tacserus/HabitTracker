package com.example.habittracker.presentation.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.domain.models.Priority
import com.example.domain.models.Type
import com.example.habittracker.R
import com.example.habittracker.presentation.compose.addHabitScreen.AddHabitScreen
import com.example.habittracker.presentation.compose.habitListScreen.HabitsPagerScreen
import com.example.habittracker.presentation.compose.infoScreen.InfoScreen
import com.example.habittracker.presentation.viewmodels.AddHabitViewModel
import com.example.habittracker.presentation.viewmodels.AddHabitViewModelFactory
import com.example.habittracker.presentation.viewmodels.HabitListViewModel
import com.example.habittracker.presentation.viewmodels.HabitListViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeAppNavigation(
    habitListViewModelFactory: HabitListViewModelFactory,
    addHabitViewModelFactory: AddHabitViewModelFactory
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val activityViewModelStoreOwner = LocalContext.current as ViewModelStoreOwner

    val habitIdKey = stringResource(R.string.habit_id)

    val habitListViewModel: HabitListViewModel = viewModel(
        viewModelStoreOwner = activityViewModelStoreOwner,
        factory = habitListViewModelFactory
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Text(stringResource(R.string.menu), modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.my_habits)) },
                    selected = navController.currentDestination?.route == Router.HabitsPager.route,
                    onClick = {
                        navController.navigate(Router.HabitsPager.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.info_about_app_title)) },
                    selected = navController.currentDestination?.route == Router.Info.route,
                    onClick = {
                        navController.navigate(Router.Info.route)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.my_habits)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.open_menu))
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Router.HabitsPager.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Router.HabitsPager.route) {
                    HabitsPagerScreen(
                        habitListViewModel = habitListViewModel,
                        onOpenAddHabitScreen = {
                            navController.navigate(Router.AddEditHabit.createRoute(null))
                        },
                        onOpenUpdateHabitScreen = { habitId ->
                            navController.navigate(Router.AddEditHabit.createRoute(habitId))
                        },
                        onCompleteHabit = { habitId ->
                            habitListViewModel.saveDoneMark(habitId)
                        },
                        onDeleteHabit = { habitId ->
                            habitListViewModel.deleteHabit(habitId)
                        }
                    )
                }


                composable(
                    route = "${Router.AddEditHabit.route}?$habitIdKey={$habitIdKey}",
                    arguments = listOf(
                        navArgument(habitIdKey) {
                            type = androidx.navigation.NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val habitId = backStackEntry.arguments?.getString(habitIdKey)
                    val addHabitViewModel: AddHabitViewModel = viewModel(
                        viewModelStoreOwner = backStackEntry,
                        factory = addHabitViewModelFactory
                    )
                    addHabitViewModel.initState(habitId, Priority.Lite.description, Type.GoodHabit.description)
                    AddHabitScreen(
                        habitId = habitId,
                        addHabitViewModel = addHabitViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Router.Info.route) {
                    InfoScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}