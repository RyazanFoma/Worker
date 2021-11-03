package com.pushe.worker.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

@Composable
fun ScanScreen(statusText: String? = null, scopeAction: (String) -> Unit) {
    val context = LocalContext.current
    var scanFlag by remember { mutableStateOf(false) }
    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {

            val capture = CaptureManager(context as Activity, this)
            val beepManager = BeepManager(context)
            capture.initializeFromIntent(context.intent, null)
            statusText?.let { this.setStatusText(it) }
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            capture.decode()
            this.decodeContinuous { result ->
                if(scanFlag){
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let { barCodeOrQr->
                    if (!context.isDestroyed) {
                        Log.i("ScanScreen", "BarCode $barCodeOrQr") /* TODO: Remove Log.i */
                        beepManager.playBeepSoundAndVibrate()
                        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                        scopeAction(barCodeOrQr)
                    }
                }
                scanFlag = false
                capture.onPause()
                capture.onDestroy()
            }
            this.resume()
        }
    }

    AndroidView(
        modifier = Modifier,
        factory = { compoundBarcodeView },
    )
}