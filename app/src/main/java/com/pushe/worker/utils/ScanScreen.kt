package com.pushe.worker.utils

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.pushe.worker.BuildConfig
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    statusText: String? = null,
    backgroundMode: Boolean = false,
    scopeAction: (barCode: String) -> Unit
) {
    val context = LocalContext.current
    var scanFlag by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            val beepManager = BeepManager(context)
            val stopScan = {
                barcodeView.stopDecoding()
                pause()
                capture.onDestroy()
            }

            capture.initializeFromIntent(context.intent, null)
            capture.decode()
            decodeContinuous { result ->
                if(scanFlag){
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let { barCode ->
                    if (!(backgroundMode || context.isDestroyed)) {
                        if (BuildConfig.DEBUG)
                            Log.i("ScanScreen", "BarCode $barCode")
                        if (barCode.length > 20) {
                            this@apply.setStatusText("ФОРМАТ ШТРИХ-КОДА НЕ ПОДДЕРЖИВАЕТСЯ!!!")
                        }
                        else {
                            beepManager.playBeepSoundAndVibrate()
                            scopeAction(barCode)
                            this@apply.setStatusText((statusText?:""))
                        }
                    }
                    scanFlag = false
                }
                scope.cancel()
                stopScan()
            }
            resume()
            scope.launch {
                repeat(16) { times ->
                    this@apply.setStatusText((statusText?:"Осталось") + " - ${15 - times}")
                    delay(1_000L)
                }
                stopScan()
                context.onBackPressed()
            }
        }
    }
    AndroidView(
        modifier = modifier,
        factory = { compoundBarcodeView },
    )
}


