package com.pushe.worker.logup

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pushe.worker.logup.model.LogUpViewModel
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
                Surface(color = MaterialTheme.colors.primary) { LogUp(context = this) }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun LogUp(context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LogUp") {
        composable("LogUp") {
            LogUp(navController)
        }
        composable("LogUp/{barCode}") { backStackEntry ->
            val barCode = backStackEntry.arguments?.getString("barCode") ?: "null"
            val viewModel: LogUpViewModel =
                viewModel(factory = LogUpViewModelFactory(context = context))
            val onRefresh = { viewModel.load(barcode = barCode) }
            onRefresh()
            LogUp(
                navController,
                viewModel.status,
                viewModel.userName,
                viewModel.error,
                onRefresh
            )
        }
        composable("ScanScreen") { ScanScreen(navController) }
        /*...*/
    }
}