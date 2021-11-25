package com.pushe.worker.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    statusText: String? = null,
    scopeAction: (barCode: String) -> Unit
) {
    val context = LocalContext.current
    var scanFlag by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            val beepManager = BeepManager(context)

            capture.initializeFromIntent(context.intent, null)
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            capture.decode()

            this.resume()
            this.decodeContinuous { result ->
                if(scanFlag){
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let { barCode ->
                    if (!context.isDestroyed) {
                        Log.i("ScanScreen", "BarCode $barCode") /* TODO: Remove Log.i */
                        beepManager.playBeepSoundAndVibrate()
                        scopeAction(barCode)
                    }
                    scanFlag = false
                }
                scope.cancel()
                this@apply.barcodeView.stopDecoding()
                capture.onPause()
                capture.onDestroy()
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            }

            scope.launch {
                repeat(16) { times ->
                    this@apply.setStatusText((statusText?:"Осталось") + " - ${15 - times}")
                    delay(1_000)
                }
                this@apply.barcodeView.stopDecoding()
                capture.onPause()
                capture.onDestroy()
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                context.onBackPressed()
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { compoundBarcodeView },
    )
}
