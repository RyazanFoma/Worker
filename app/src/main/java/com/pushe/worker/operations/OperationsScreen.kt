package com.pushe.worker.operations

import android.content.res.Configuration
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
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pushe.worker.R
import com.pushe.worker.operations.model.*
import com.pushe.worker.operations.ui.ListScreen
import com.pushe.worker.operations.ui.OperationScan
import com.pushe.worker.operations.ui.TotalsScreen
import com.pushe.worker.operations.ui.OperationScreen
import com.pushe.worker.utils.ScanScreen

private enum class Navigate(val route: String) {
    List("List"),
    Totals("Totals"),
    Scanner("Scanner"),
    Operation("Operation")
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun OperationsScreen(
    userId: String,
    userName: String,
) {
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
                val viewModel: ListViewModel = viewModel(
                    factory = ListViewModelFactory(userId = userId)
                )
                ListScreen(operationsFlow = viewModel.operationsFlow, isRefreshing = false)
            }
            composable(Navigate.Totals.route) {
                val orientation = LocalConfiguration.current.orientation
                val viewModel: TotalsViewModel = viewModel(
                    factory = TotalsViewModelFactory(userId = userId)
                )
                viewModel.setAnalytics(analyticsNew = when(orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> TotalsViewModel.Analytics.TIME
                        else -> TotalsViewModel.Analytics.TYPE //Configuration.ORIENTATION_PORTRAIT
                    }
                )
                TotalsScreen(
                    status = viewModel.status,
                    orientation = orientation,
                    bars = viewModel.bars,
                    title = viewModel.title,
                    error = viewModel.error,
                    startTab = viewModel.period.periodSize,
                    onSelectTab = viewModel::changePeriodSize,
                    onLeftShift = viewModel::nextPeriod,
                    onRightShift = viewModel::previousPeriod,
                    onRefresh = viewModel::load,
                )
            }
            composable(
                route = Navigate.Scanner.route
            ) {
                val viewModel: OperationViewModel = viewModel() //TODO: add factory = OperationViewModelFactory

                OperationScan(userId = userId, viewModel = viewModel) {
                    navController.navigate(Navigate.List.route) {
                        popUpTo(Navigate.List.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
//                ScanScreen(statusText = "Штрих код операции") { barCode ->
//                    navController.navigate( Navigate.Operation.route + "/" + barCode){
//                        popUpTo(Navigate.List.route) { saveState = true }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
            }
            composable(
                route = Navigate.Operation.route + "/{barCode}",
                arguments = listOf(navArgument("barCode") { type = NavType.StringType }
                )
            ) { entry ->
                val viewModel: OperationViewModel = viewModel() //TODO: add factory = OperationViewModelFactory

                OperationScreen(
                    userId = userId,
                    barCode = entry.arguments?.getString("barCode"),
                    status = viewModel.status,
                    operation = viewModel.operation,
                    error = viewModel.error,
                    onRefresh = viewModel::load,
                    onCompleted = viewModel::completed,
                ) {
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
