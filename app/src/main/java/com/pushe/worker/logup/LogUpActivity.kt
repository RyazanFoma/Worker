package com.pushe.worker.logup

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pushe.worker.logup.model.LogUpViewModel
import com.pushe.worker.logup.ui.LogUp
import com.pushe.worker.operations.OperationsScreen
import com.pushe.worker.operations.model.ListViewModel
import com.pushe.worker.operations.model.OperationViewModel
import com.pushe.worker.operations.model.TotalsViewModel
import com.pushe.worker.operations.theme.WorkerTheme
import com.pushe.worker.settings.SettingsScreen
import com.pushe.worker.settings.model.SettingsViewModel
import com.pushe.worker.utils.ScanScreen
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class LogUpActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val logUpViewModel: LogUpViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()
    private val totalsViewModel: TotalsViewModel by viewModels()
    private val operationViewModel: OperationViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkerTheme {
                Navigation(
                    settingsViewModel = settingsViewModel,
                    logUpViewModel = logUpViewModel,
                    listViewModel = listViewModel,
                    totalsViewModel = totalsViewModel,
                    operationViewModel = operationViewModel,
                )
            }
        }
    }
}

private enum class Navigate(val route: String) {
    LogUp("LogUp"),
    Scanner("Scanner"),
    Setting("Setting"),
    LogIn("LogIn/"),
    Operations("Operations/"),
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun Navigation(
    settingsViewModel: SettingsViewModel,
    logUpViewModel: LogUpViewModel,
    listViewModel: ListViewModel,
    totalsViewModel: TotalsViewModel,
    operationViewModel: OperationViewModel,
) {
    val context = LocalContext.current as Activity
    val navController = rememberNavController()
    val onBarCode = {
        navController.navigate(Navigate.Scanner.route) {
            popUpTo(Navigate.LogUp.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val onSetting = {
        navController.navigate(Navigate.Setting.route) {
            popUpTo(Navigate.LogUp.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(navController = navController, startDestination = Navigate.LogUp.route) {
        composable(Navigate.LogUp.route) {
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            LogUp(onBarCode = onBarCode, onSetting = onSetting)
        }
        composable(
            route = Navigate.LogIn.route + "{barCode}",
            arguments = listOf(navArgument("barCode") { type = NavType.StringType })
        ) { entry ->
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            LogUp(
                onBarCode = onBarCode,
                onSetting = onSetting,
                viewModel = logUpViewModel,
                onRefresh = entry.arguments?.getString("barCode")?.let { code -> { logUpViewModel.load(code) } },
            ) { userId, userName ->
                    navController.navigate(Navigate.Operations.route +
                            "userId=$userId&userName=$userName") {
                        popUpTo(Navigate.LogUp.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
            }
        }
        composable(Navigate.Scanner.route) {
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            ScanScreen(statusText = "Штрих код сотрудника") { barCode ->
                barCode.let{ code -> logUpViewModel.load(code) }
                navController.navigate(Navigate.LogIn.route + barCode) {
                    popUpTo(Navigate.LogUp.route)
                    launchSingleTop = true
                }
            }
        }
        composable(Navigate.Setting.route) {
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            SettingsScreen(settingsViewModel)
        }
        composable(Navigate.Operations.route + "userId={userId}&userName={userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType },
            )
        ) { entry ->
            OperationsScreen(
                userId = entry.arguments?.getString("userId")!!,
                userName = entry.arguments?.getString("userName")!!,
                settingsViewModel = settingsViewModel,
                listViewModel = listViewModel,
                totalsViewModel = totalsViewModel,
                operationViewModel = operationViewModel,
            )
        }
    }
}