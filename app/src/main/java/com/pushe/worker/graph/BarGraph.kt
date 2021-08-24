package com.pushe.worker.graph

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.min
import kotlin.math.pow

class BarGraph(val bars: List<Bar>) {

    data class Bar(
        val label: String,
        val value: Float
    )

    /**
     * Label column width by pixel and character
     */
    private data class Width(
        val byPixel: Float,
        val byChar: Int
    )

    companion object {
        const val amountSections = 5
        const val maxAmountBars = 31
        const val betweenBars = 10f
        const val fontSize = 24f
        const val marginTop = 8f
        const val marginBottom = 24f
        const val marginLeft = 96f
    }

    /**
     * Size and step of the grid in units of a bar.value
     */
    private var sizeGrid: Float
    private var stepGrid: Float
    init {
        var max = amountSections.toFloat()
        for (bar in bars) { if (bar.value > max) { max = bar.value } }
        val dec = 10.0f.pow(ceil(log10(max + 0.5f).toDouble()).toInt() - 1)
        sizeGrid = ceil(max / dec + 0.5f) * dec
        stepGrid = sizeGrid/amountSections
    }

    /**
     * Draws a horizontal bar chart
     */
    @Composable
    fun Horizontal(modifier: Modifier,
                   colorLine: Color = Color.Black,
                   colorRect: Color = Color.Magenta) {
        Canvas(modifier = modifier) {
            val offsetLT = Offset(marginLeft, marginTop)
            val size0 = Size(size.width - offsetLT.x,
                size.height - marginTop - marginBottom)
            drawHorizontalGrid(scope = this, offsetLT = offsetLT, size = size0,
                colorLine = colorLine)
            drawHorizontalBars(scope = this, offsetLT = offsetLT, size = size0,
                colorLine = colorLine, colorRect = colorRect)
        }
    }

    /**
     * Draws a vertical bar chart
     */
    @Composable
    fun Vertical(modifier: Modifier,
                 colorLine: Color = Color.Black,
                 colorRect: Color = Color.Magenta) {
        Canvas(modifier = modifier) {
            val maxWidth = maxLabels(size.width / 2,
                Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = fontSize
            })
            val offsetLT = Offset(maxWidth.byPixel, 0f)
            val size0 = Size(size.width - offsetLT.x,
                size.height - marginTop - marginBottom)
            drawVerticalGrid(scope = this, offsetLT = offsetLT, size = size0,
                colorLine = colorLine)
            drawVerticalBars(scope = this, offsetLT = offsetLT, size = size0, maxWidth.byChar,
                colorLine = colorLine, colorRect = colorRect)
        }
    }

    /**
     * Draws a grid for a horizontal bar chart
     */
    private fun drawHorizontalGrid(scope: DrawScope, offsetLT: Offset, size: Size,
                                   colorLine: Color) {
        val offsetRT = Offset(offsetLT.x + size.width, offsetLT.y)
        val offsetRB = Offset(offsetLT.x + size.width, offsetLT.y + size.height)
        val offsetLB = Offset(offsetLT.x, offsetLT.y + size.height)
        val step = size.height/amountSections
        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = fontSize
            color = colorLine.hashCode()
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        scope.drawLine(color = colorLine, start = offsetRT, end = offsetRB )
        scope.drawLine(color = colorLine, start = offsetLB, end = offsetLT )
        for (i in 0..amountSections) {
            val y = offsetLB.y - i * step
            scope.drawLine(color = colorLine,
                start = Offset(offsetLB.x, y),
                end = Offset(offsetRB.x, y) )
            scope.drawIntoCanvas {
                it.nativeCanvas.drawText(
                    "${i * stepGrid.toInt()}",
                    offsetLT.x - marginTop,
                    y + fontSize / 2,
                    textPaint)
            }
        }
    }

    /**
     * Draws a bars for a horizontal bar chart
     */
    private fun drawHorizontalBars(scope: DrawScope, offsetLT: Offset, size: Size,
                                   colorLine: Color, colorRect: Color) {
        if (bars.isEmpty()) return
        val amountBars = min(bars.size, maxAmountBars)
        val widthBar = (size.width - betweenBars * (amountBars + 1)) / amountBars
        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = fontSize
            color = colorLine.hashCode()
            textAlign = android.graphics.Paint.Align.CENTER
        }

        for (i in 0 until amountBars) {
            val heightBar = size.height * bars[i].value / sizeGrid
            val x = offsetLT.x + betweenBars + i * (widthBar + betweenBars)
            scope.drawRect(
                color = colorRect,
                topLeft = Offset(x = x, y = offsetLT.y + size.height - heightBar),
                size = Size(width = widthBar, height = heightBar)
            )
            scope.drawIntoCanvas {
                it.nativeCanvas.drawText(
                    bars[i].label,
                    x + widthBar / 2,
                    offsetLT.y + size.height + marginBottom,
                    textPaint)
            }
        }
    }

    /**
     * Draws a grid for a vertical bar chart
     */
    private fun drawVerticalGrid(scope: DrawScope, offsetLT: Offset, size: Size, colorLine: Color) {
        val offsetRT = Offset(offsetLT.x + size.width, offsetLT.y)
        val offsetRB = Offset(offsetLT.x + size.width, offsetLT.y + size.height)
        val offsetLB = Offset(offsetLT.x, offsetLT.y + size.height)
        val step = size.width/amountSections
        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = fontSize
            color = colorLine.hashCode()
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        scope.drawLine(color = colorLine, start = offsetLT, end = offsetRT )
        scope.drawLine(color = colorLine, start = offsetLB, end = offsetRB )
        for (i in 0..amountSections) {
            val x = offsetLT.x + i * step
            scope.drawLine(color = colorLine,
                start = Offset(x, offsetRT.y),
                end = Offset(x, offsetRB.y) )
            scope.drawIntoCanvas {
                it.nativeCanvas.drawText(
                    "${i * stepGrid.toInt()}",
                    x,
                    offsetRB.y + marginBottom,
                    textPaint)
            }
        }
    }

    /**
     * Draws a bars for a vertical bar chart
     */
    private fun drawVerticalBars(scope: DrawScope, offsetLT: Offset, size: Size, maxLength: Int,
                                 colorLine: Color, colorRect: Color) {
        if (bars.isEmpty()) return
        val amountBars = min(bars.size, maxAmountBars)
        val heightBar = (size.height - betweenBars * (amountBars + 1)) / amountBars
        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = fontSize
            color = colorLine.hashCode()
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        for (i in 0 until amountBars) {
            val widthBar = size.width * bars[i].value / sizeGrid
            val y = offsetLT.y + betweenBars + i * (heightBar + betweenBars)
            scope.drawRect(
                color = colorRect,
                topLeft = Offset(x = offsetLT.x, y = y),
                size = Size(width = widthBar, height = heightBar)
            )
            scope.drawIntoCanvas {
                it.nativeCanvas.drawText(
                    cutLabel(label = bars[i].label, maxLength = maxLength),
                    offsetLT.x - marginTop,
                    y + (heightBar + fontSize) / 2,
                    textPaint)
            }
        }
    }

    /**
     * Truncates a string to its maximum length
     */
    private fun cutLabel(label: String, maxLength: Int) : String {
        val len = label.length
        if (len <= maxLength) return label
        val cut = ((maxLength - 3) / 2)
        return label.replaceRange(cut until len-cut, "...")
    }

    /**
     * Determination of the required width of the column of labels, but not more than the maximum
     */
    private fun maxLabels(maxWidth : Float, textPaint: NativePaint) : Width {
        val widthG = textPaint.measureText("g")
        val limit: Int = (maxWidth / widthG).toInt()
        var max = 0
        for (bar in bars) {
            val len = bar.label.length
            if (len > limit) max = limit else if (len > max) max = len
        }
        return Width(byPixel= widthG * max, byChar = max)
    }
}