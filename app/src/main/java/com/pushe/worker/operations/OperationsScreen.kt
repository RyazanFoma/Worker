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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pushe.worker.R
import com.pushe.worker.operations.model.TotalsViewModel
import com.pushe.worker.operations.model.TotalsViewModelFactory
import com.pushe.worker.operations.ui.TotalsScreen
import com.pushe.worker.operation.ListScreen
import com.pushe.worker.operations.model.ListViewModel
import com.pushe.worker.operations.model.ListViewModelFactory
import com.pushe.worker.operation.ui.summary.OperationScreen
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
    val context = LocalContext.current
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val fabShape = RoundedCornerShape(50)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar( title = { Text(userName) } ) },
        floatingActionButton = {
            OperationsFab(
                shape = fabShape,
                onScan = { navController.navigate( route = Navigate.Scanner.route) }
            )
                               },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        bottomBar = { OperationsBottomBar(
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
            }
        )
                    },
    ) { innerPadding ->
        NavHost(navController, startDestination = Navigate.List.route, Modifier.padding(innerPadding)) {
            composable(Navigate.List.route) {
                val viewModel: ListViewModel = viewModel(
                    factory = ListViewModelFactory(context, userId = userId)
                )
                ListScreen(operationsFlow = viewModel.operationsFlow, isRefreshing = false)
            }
            composable(Navigate.Totals.route) {
                val orientation = LocalConfiguration.current.orientation
                val viewModel: TotalsViewModel = viewModel(
                    factory = TotalsViewModelFactory(context, userId = userId)
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
                    onRefresh = viewModel::loadTotals
                )
            }
            composable(
                route = Navigate.Scanner.route
            ) {
                ScanScreen(statusText = "Штрих код операции") { barCode ->
                    navController.navigate( Navigate.Operation.route + "/" + barCode)
                }
            }
            composable(
                route = Navigate.Operation.route + "/{barCode}",
                arguments = listOf(navArgument("barCode") { type = NavType.StringType }
                )
            ) { entry ->
                OperationScreen(
                    userId = userId,
                    barcode = entry.arguments?.getString("barCode") ?: "null"
                )
            }
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

//@ExperimentalFoundationApi
//@ExperimentalMaterialApi
//@Preview
//@Composable
//fun Preview() {
//    OperaScreen(userId = "0001", userName ="Иванов Иван Иванович", {}, )
//}