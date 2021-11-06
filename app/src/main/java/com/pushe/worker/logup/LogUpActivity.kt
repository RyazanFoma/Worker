package com.pushe.worker.logup

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pushe.worker.logup.model.LogUpViewModelFactory
import com.pushe.worker.logup.ui.LogUp
import com.pushe.worker.settings.SettingsScreen
import com.pushe.worker.settings.model.SettingsViewModelFactory
import com.pushe.worker.theme.WorkerTheme
import com.pushe.worker.utils.*

private const val ACCOUNT_PREFERENCE_NAME = "settings"
private val Context.dataStore by preferencesDataStore(
    name = ACCOUNT_PREFERENCE_NAME
)

class LogUpActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkerTheme {
                Navigation(dataStore = dataStore)
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun Navigation(dataStore: DataStore<Preferences>) {
    val context = LocalContext.current
    val navController = rememberNavController()

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
                viewModel = viewModel(factory = LogUpViewModelFactory(context = context)),
                barCode = entry.arguments?.getString("barCode"),
                onSetting = { navController.navigate( "Setting") },
            ) { userId, userName ->
                Log.i("LogUp", "Go with userId=$userId userName=$userName") /* TODO: go to operations list */
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
            SettingsScreen(viewModel(factory = SettingsViewModelFactory(dataStore = dataStore)))
        }
        /*...*/
    }
}