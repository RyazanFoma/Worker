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

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    AccountRepository.initPreference(context.dataStore)
    NavHost(navController = navController, startDestination = "LogUp") {
        composable("LogUp") {
            LogUp(
                onBarCode = { navController.navigate("ScanScreen/LogUp") },
                onSetting = { navController.navigate( "Setting") },
            )
        }
        composable(
            route = "LogUp/{barCode}",
            arguments = listOf(navArgument("barCode") { type = NavType.StringType })
        ) { entry ->
            LogUp(
                onBarCode = {
                    navController.navigate("ScanScreen/LogUp") {
                        popUpTo("LogUp/{barCode}") { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = LogUpViewModelFactory()),
                barCode = entry.arguments?.getString("barCode"),
                onSetting = { navController.navigate( "Setting") },
            ) { userId, userName ->
                    navController.navigate("Operations/userId=$userId&userName=$userName")
            }
        }
        composable("ScanScreen/LogUp") { barCode ->
            ScanScreen("Штрих код сотрудника") {
                navController.navigate(route = "LogUp/$barCode") {
                    popUpTo("ScanScreen/LogUp") { inclusive = true }
                }
            }
        }
        composable("Setting") {
            SettingsScreen(viewModel(factory = SettingsViewModelFactory(context.dataStore)))
        }
        composable("Operations/userId={userId}&userName={userName}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                },
                navArgument("userName") {
                    type = NavType.StringType
                },
            )
        ) { entry ->
            OperationsScreen(
                userId = entry.arguments?.getString("userId") ?: "null",
                userName = entry.arguments?.getString("userName") ?: "null",
            )
        }
    }
}