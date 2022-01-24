package com.pushe.worker.operations

//import androidx.navigation.navArgument
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pushe.worker.R
import com.pushe.worker.operations.model.ListViewModel
import com.pushe.worker.operations.model.OperationViewModel
import com.pushe.worker.operations.model.TotalsViewModel
import com.pushe.worker.operations.ui.ListScreen
import com.pushe.worker.operations.ui.OperationScan
import com.pushe.worker.operations.ui.TotalsScreen
import com.pushe.worker.settings.model.SettingsViewModel

private enum class Navigate(val route: String) {
    List("List"),
    Totals("Totals"),
    Scanner("Scanner"),
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun OperationsScreen(
    userId: String,
    userName: String,
    settingsViewModel: SettingsViewModel,
    listViewModel: ListViewModel,
    totalsViewModel: TotalsViewModel,
    operationViewModel: OperationViewModel,
) {
    val context = LocalContext.current as Activity
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val fabShape = RoundedCornerShape(25)
    val visibilityBottomBar = rememberSaveable { mutableStateOf(true) }
    val visibilityFab = rememberSaveable { mutableStateOf(true) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar( title = { Text(userName) } ) },
        floatingActionButton = { if (visibilityFab.value) {
                OperationsFab(
                    shape = fabShape,
                    onScan = { navController.navigate( route = Navigate.Scanner.route) {
                            popUpTo(Navigate.List.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        bottomBar = { if (visibilityBottomBar.value) {
                OperationsBottomBar(
                    fabShape = fabShape,
                    onList = {
                        navController.navigate(Navigate.List.route) {
                            popUpTo(Navigate.List.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onTotals = {
                        navController.navigate(Navigate.Totals.route) {
                            popUpTo(Navigate.List.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(navController, startDestination = Navigate.List.route, Modifier.padding(innerPadding)) {
            composable(Navigate.List.route) {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                listViewModel.set(userId)
                ListScreen(
                    operationsFlow = listViewModel.operationsFlow,
                    isRefreshing = false,
                    showHelp = settingsViewModel::showSwipeDown
                )
            }
            composable(Navigate.Totals.route) {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                val orientation = LocalConfiguration.current.orientation
                totalsViewModel.setAnalytics(
                    userId = userId,
                    analyticsNew = when(orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> TotalsViewModel.Analytics.TIME
                        else -> TotalsViewModel.Analytics.TYPE //Configuration.ORIENTATION_PORTRAIT
                    }
                )
                TotalsScreen(
                    userId = userId,
                    status = totalsViewModel.status,
                    orientation = orientation,
                    bars = totalsViewModel.bars,
                    title = totalsViewModel.title,
                    error = totalsViewModel.error,
                    startTab = totalsViewModel.period.periodSize,
                    onSelectTab = totalsViewModel::changePeriodSize,
                    onLeftShift = totalsViewModel::nextPeriod,
                    onRightShift = totalsViewModel::previousPeriod,
                    onRefresh = totalsViewModel::load,
                    showHelp = settingsViewModel::showSwipeRotation
                )
            }
            composable(
                route = Navigate.Scanner.route
            ) {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                OperationScan(userId = userId, viewModel = operationViewModel) {
                    navController.navigate(Navigate.List.route) {
                        popUpTo(Navigate.List.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            visibilityBottomBar.value = destination.route == Navigate.List.route ||
                    destination.route == Navigate.Totals.route
            visibilityFab.value = destination.route != Navigate.Scanner.route
        }
    }
}

@Composable
private fun OperationsFab(shape: Shape, onScan: () -> Unit) {
    FloatingActionButton(
        onClick = onScan,
        shape = shape,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_barcode),
            contentDescription = "Scanner",
        )
    }
}

@Composable
private fun OperationsBottomBar(
    fabShape: Shape,
    onList: () -> Unit,
    onTotals: () -> Unit,
) {
    BottomAppBar(cutoutShape = fabShape) {
        IconButton(onClick = onList) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "List"
            )
        }
        IconButton(onClick = onTotals) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "Totals"
            )
        }
    }
}
