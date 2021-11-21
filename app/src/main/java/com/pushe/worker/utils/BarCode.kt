package com.pushe.worker.utils

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

@Composable
fun BarCode(modifier: Modifier = Modifier, barCode: String, color: Color = Color.Black) {
    val writer = MultiFormatWriter()
    val finalBarCode = Uri.encode(barCode, "utf-8")
    Canvas(modifier = modifier) {
        val bitMatrix = writer.encode(finalBarCode, BarcodeFormat.CODE_128, size.width.toInt(), 1)
        var started = false
        var topLeft = Offset(0f, 0f)
        for (i in 0 until bitMatrix.width) {
            when {
                bitMatrix[i, 0] && !started -> {
                    started = true
                    topLeft = Offset(size.width * i / bitMatrix.width, 0f)
                }
                !bitMatrix[i, 0] && started -> {
                    started = false
                    drawRect(color, topLeft,
                        Size(size.width * i / bitMatrix.width - topLeft.x, size.height))
                }
            }
        }
        if (started) drawRect(color, topLeft, Size(size.width - topLeft.x, size.height))
    }
}

@Preview
@Composable
fun Preview() {
    BarCode(modifier = Modifier.fillMaxSize(), barCode = "C764")
}