package com.pushe.worker.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Function to create a Custom Image Chip with text
// text - For showing data on the chip
// imageId - For showing the image that we want to use
// selected : Boolean - to check if it is selected or not
@Composable
fun Chip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    icon: @Composable ((modifier: Modifier) -> Unit)? = null,
) {
    // define properties to the chip
    // such as color, shape, width
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary
            else -> Color.Transparent
        },
        contentColor = when {
            selected -> MaterialTheme.colors.onPrimary
            else -> Color.LightGray
        },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> MaterialTheme.colors.primary
                else -> Color.LightGray
            }
        ),
        modifier = modifier
    ) {
        // Inside a Row pack the Image and text together to
        // show inside the chip
        Row(modifier = Modifier) {
            icon?.let { it(modifier = Modifier
                .padding(8.dp)
                .size(20.dp)
                .clip(CircleShape)) }
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            )
        }
    }
}