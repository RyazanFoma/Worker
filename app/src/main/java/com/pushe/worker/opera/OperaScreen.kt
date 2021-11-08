package com.pushe.worker.opera

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pushe.worker.R
import com.pushe.worker.operations.OperationsScreen
import com.pushe.worker.operations.OperationsViewModel
import com.pushe.worker.operations.OperationsViewModelFactory
import com.pushe.worker.totals.TotalsScreen
import com.pushe.worker.totals.TotalsViewModel
import com.pushe.worker.totals.TotalsViewModelFactory

private enum class Navigate(val route: String) { Operations("Operations"), Totals("Totals") }

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun OperaScreen(
    userId: String,
    userName: String,
    onScan: () -> Unit,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val fabShape = RoundedCornerShape(50)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar( title = { Text(userName) } ) },
        floatingActionButton = { OperationsFab(shape = fabShape, onScan = onScan) },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        bottomBar = { OperationsBottomBar(
            fabShape = fabShape,
            onList = {
                navController.navigate(Navigate.Operations.route) {
                    popUpTo(Navigate.Operations.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
                     },
            onTotals = {
                navController.navigate(Navigate.Totals.route) {
                    popUpTo(Navigate.Operations.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
                    },
    ) { innerPadding ->
        NavHost(navController, startDestination = "Operations", Modifier.padding(innerPadding)) {
            composable(Navigate.Operations.route) {
                val viewModel: OperationsViewModel = viewModel(
                    factory = OperationsViewModelFactory(context, userId = userId)
                )
                OperationsScreen(operationsFlow = viewModel.operationsFlow, isRefreshing = false)
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