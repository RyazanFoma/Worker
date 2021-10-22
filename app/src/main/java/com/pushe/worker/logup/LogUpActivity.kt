package com.pushe.worker.logup

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pushe.worker.logup.model.LogUpViewModel
import com.pushe.worker.logup.model.LogUpViewModelFactory
import com.pushe.worker.logup.ui.LogUp
import com.pushe.worker.theme.WorkerTheme

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
    val viewModel: LogUpViewModel = viewModel(factory = LogUpViewModelFactory(context = context))

    LogUp(
        userName = viewModel.userName,
        onClickButton = viewModel::onScan,
        onLogIn = {},
        onLogOut = {}
    )
}