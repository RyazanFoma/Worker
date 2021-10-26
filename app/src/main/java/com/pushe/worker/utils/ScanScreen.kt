package com.pushe.worker.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

@Composable
fun ScanScreen(navController: NavHostController) {
    val context = LocalContext.current
//    var scanFlag by remember { mutableStateOf(false) }
    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("Статус текст")
            this.resume()
            capture.decode()
            this.decodeSingle { result ->
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                result.text?.let { barCodeOrQr ->
                    navController.navigate(route = "LogUp/$barCodeOrQr" )
                }
                capture.onDestroy()
            }
        }
    }

    AndroidView(
        modifier = Modifier,
        factory = { compoundBarcodeView },
    )
}