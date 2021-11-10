package com.pushe.worker.operation.ui.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pushe.worker.R

@Composable
fun ResultText(text: String, colors: List<Color>, borderColor: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.h3.copy(color = colorResource(id = R.color.white)),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .border(
                width = 4.dp,
                color = borderColor,
                RoundedCornerShape(32.dp)
            )
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = RoundedCornerShape(32.dp)
                clip = true
            }
            .background(
                brush = Brush.horizontalGradient(
                    colors = colors
                )
            )
            .padding(32.dp)
    )
}