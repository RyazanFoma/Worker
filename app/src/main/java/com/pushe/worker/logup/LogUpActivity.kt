package com.pushe.worker.logup

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pushe.worker.logup.model.LogUpViewModelFactory
import com.pushe.worker.logup.ui.LogUp
import com.pushe.worker.operations.OperationsScreen
import com.pushe.worker.operations.theme.WorkerTheme
import com.pushe.worker.settings.SettingsScreen
import com.pushe.worker.settings.data.AccountRepository
import com.pushe.worker.settings.model.SettingsViewModelFactory
import com.pushe.worker.utils.*

private const val ACCOUNT_PREFERENCE_NAME = "settings"
private val Context.dataStore by preferencesDataStore(
    name = ACCOUNT_PREFERENCE_NAME
)

class LogUpActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkerTheme {
                Navigation()
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
private fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    AccountRepository.initPreference(context.dataStore)
    NavHost(navController = navController, startDestination = Navigate.LogUp.route) {
        composable(Navigate.LogUp.route) {
            LogUp(
                onBarCode = {
                    navController.navigate(Navigate.Scanner.route) {
                        popUpTo(Navigate.LogUp.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                            },
                onSetting = {
                    navController.navigate(Navigate.Setting.route) {
                        popUpTo(Navigate.LogUp.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                            },
            )
        }
        composable(
            route = Navigate.LogIn.route + "{barCode}",
            arguments = listOf(navArgument("barCode") { type = NavType.StringType })
        ) { entry ->
            LogUp(
                onBarCode = {
                    navController.navigate(Navigate.Scanner.route) {
                        popUpTo(Navigate.LogUp.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                viewModel = viewModel(factory = LogUpViewModelFactory()),
                barCode = entry.arguments?.getString("barCode"),
                onSetting = {
                    navController.navigate(Navigate.Setting.route) {
                        popUpTo(Navigate.LogUp.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                            },
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
            ScanScreen(statusText = "Штрих код сотрудника") { barCode ->
                navController.navigate(Navigate.LogIn.route + barCode) {
                    popUpTo(Navigate.LogUp.route)
                    launchSingleTop = true
                }
            }
        }
        composable(Navigate.Setting.route) {
            SettingsScreen(viewModel(factory = SettingsViewModelFactory(context.dataStore)))
        }
        composable(Navigate.Operations.route + "userId={userId}&userName={userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType },
            )
        ) { entry ->
            OperationsScreen(
                userId = entry.arguments?.getString("userId") ?: "null",
                userName = entry.arguments?.getString("userName") ?: "null",
            )
        }
    }
}