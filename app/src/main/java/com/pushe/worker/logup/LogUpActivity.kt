package com.pushe.worker.logup

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pushe.worker.logup.model.LogUpViewModelFactory
import com.pushe.worker.logup.ui.LogUp
import com.pushe.worker.theme.WorkerTheme
import com.pushe.worker.utils.ScanScreen

class LogUpActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.primary) { Navigation() }
            }
        }
    }
    override fun onBackPressed() {
        Log.i("LogUpActivity", "finish()") /* TODO: Remove Log.i */
        finish()
    }
}

@ExperimentalAnimationApi
@Composable
private fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LogUp") {
        composable("LogUp") {
            LogUp(
                onBarCode = {
                    navController.navigate("ScanScreen/LogUp") {
                        Log.i("LogUp", " -> ScanScreen/LogUp popUpTo LogUp inc") /* TODO: Remove Log.i */
                        popUpTo("LogUp") { inclusive = true }
//                        launchSingleTop = true
                    }
                },
            )
        }
        composable(
            route = "LogUp/{barCode}",
            arguments = listOf(navArgument("barCode") { type = NavType.StringType })
        ) { entry ->
            LogUp(
                onBarCode = {
                    navController.navigate("ScanScreen/LogUp") {
//                        launchSingleTop = true
                        Log.i("LogUp/{barCode}", " -> ScanScreen/LogUp popUpTo LogUp/{barCode} inc") /* TODO: Remove Log.i */
                        popUpTo("LogUp/{barCode}") { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = LogUpViewModelFactory(context = context)),
                barCode = entry.arguments?.getString("barCode"),
            ) { userId ->
                Log.i("LogUp", "Go with userId=$userId") /* TODO: go to operations list */
            }
        }
        composable("ScanScreen/LogUp") {
            ScanScreen("Штрих код сотрудника") {
                barCode -> if (!(context as Activity).isDestroyed)
                    navController.navigate(route = "LogUp/$barCode") {
                        Log.i("ScanScreen/LogUp", " -> LogUp/$barCode popUpTo ScanScreen/LogUp inc") /* TODO: Remove Log.i */
                        popUpTo("ScanScreen/LogUp") { inclusive = true }
                    }
            }
        }
        /*...*/
    }
}