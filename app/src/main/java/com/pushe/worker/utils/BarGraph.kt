package com.pushe.worker.utils

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

/**
 * Rectangular bar graph
 * @param label - signature at the bottom or left of the bar
 * @param value - bar value
 */
data class Bar(
    val label: String,
    var value: Float = 0f
)

/**
 * Graph options
 * @param amountSections - number of grid sections of the graph
 */
data class OptionsGraph(
    val amountSections: Int = 5,
    val maxAmountBars: Int = 31,
    val betweenBars: Float = 10f,
    val fontSize: Float = 24f,
    val marginTop: Float = 8f,
    val marginBottom: Float = 24f,
    val marginLeft: Float = 96f,
)

/**
 * Draws a horizontal bar chart
 */
@Composable
fun Horizontal(bars: List<Bar>,
               modifier: Modifier,
               colorLine: Color = Color.Black,
               colorRect: Color = Color.Magenta,
               optionsGraph: OptionsGraph = OptionsGraph() ) {

    Canvas(modifier = modifier) {

        val offsetLT = Offset(optionsGraph.marginLeft, optionsGraph.marginTop)
        val size0 = Size(size.width - offsetLT.x,
            size.height - optionsGraph.marginTop - optionsGraph.marginBottom)
        val sizeGrid = sizeGrid(bars, optionsGraph.amountSections.toFloat())

        drawHorizontalGrid(scope = this,
            offsetLT = offsetLT,
            size = size0,
            colorLine = colorLine,
            stepGrid = sizeGrid / optionsGraph.amountSections,
            optionsGraph = optionsGraph)

        drawHorizontalBars(bars = bars,
            scope = this,
            offsetLT = offsetLT,
            size = size0,
            colorLine = colorLine,
            colorRect = colorRect,
            sizeGrid = sizeGrid,
            optionsGraph = optionsGraph)
    }
}

/**
 * Draws a vertical bar chart
 */
@Composable
fun Vertical(bars: List<Bar>,
             modifier: Modifier,
             colorLine: Color = Color.Black,
             colorRect: Color = Color.Magenta,
             optionsGraph: OptionsGraph = OptionsGraph()) {

    Canvas(modifier = modifier) {

        val maxWidth = maxLabels( bars, size.width / 2,
            Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = optionsGraph.fontSize
            })
        val offsetLT = Offset(maxWidth.byPixel, 0f)
        val size0 = Size(size.width - offsetLT.x,
            size.height - optionsGraph.marginTop - optionsGraph.marginBottom)
        val sizeGrid = sizeGrid(bars, optionsGraph.amountSections.toFloat())

        drawVerticalGrid(scope = this,
            offsetLT = offsetLT,
            size = size0,
            colorLine = colorLine,
            stepGrid = sizeGrid / optionsGraph.amountSections,
            optionsGraph = optionsGraph)

        drawVerticalBars(bars = bars,
            scope = this,
            offsetLT = offsetLT,
            size = size0,
            maxLength = maxWidth.byChar,
            colorLine = colorLine,
            colorRect = colorRect,
            sizeGrid = sizeGrid,
            optionsGraph = optionsGraph)
    }
}

/**
 * Draws a grid for a horizontal bar chart
 */
private fun drawHorizontalGrid(scope: DrawScope, offsetLT: Offset, size: Size,
                               colorLine: Color, stepGrid: Int, optionsGraph: OptionsGraph) {

    val offsetRT = Offset(offsetLT.x + size.width, offsetLT.y)
    val offsetRB = Offset(offsetLT.x + size.width, offsetLT.y + size.height)
    val offsetLB = Offset(offsetLT.x, offsetLT.y + size.height)
    val step = size.height/ optionsGraph.amountSections
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = optionsGraph.fontSize
        color = colorLine.hashCode()
        textAlign = android.graphics.Paint.Align.RIGHT
    }

    scope.drawLine(color = colorLine, start = offsetRT, end = offsetRB )
    scope.drawLine(color = colorLine, start = offsetLB, end = offsetLT )
    for (i in 0..optionsGraph.amountSections) {
        val y = offsetLB.y - i * step
        scope.drawLine(color = colorLine,
            start = Offset(offsetLB.x, y),
            end = Offset(offsetRB.x, y) )
        scope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                "${i * stepGrid}",
                offsetLT.x - optionsGraph.marginTop,
                y + optionsGraph.fontSize / 2,
                textPaint)
        }
    }
}

/**
 * Draws a bars for a horizontal bar chart
 */
private fun drawHorizontalBars(bars: List<Bar>, scope: DrawScope, offsetLT: Offset, size: Size,
                               colorLine: Color, colorRect: Color, sizeGrid: Int,
                               optionsGraph: OptionsGraph) {

    if (bars.isEmpty()) return

    val amountBars = min(bars.size, optionsGraph.maxAmountBars)
    val widthBar = (size.width - optionsGraph.betweenBars * (amountBars + 1)) / amountBars
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = optionsGraph.fontSize
        color = colorLine.hashCode()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    for (i in 0 until amountBars) {
        val heightBar = size.height * bars[i].value / sizeGrid
        val x = offsetLT.x + optionsGraph.betweenBars + i * (widthBar + optionsGraph.betweenBars)
        scope.drawRect(
            color = colorRect,
            topLeft = Offset(x = x, y = offsetLT.y + size.height - heightBar),
            size = Size(width = widthBar, height = heightBar)
        )
        scope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                bars[i].label,
                x + widthBar / 2,
                offsetLT.y + size.height + optionsGraph.marginBottom,
                textPaint)
        }
    }
}

/**
 * Draws a grid for a vertical bar chart
 */
private fun drawVerticalGrid(scope: DrawScope, offsetLT: Offset, size: Size, colorLine: Color,
                             stepGrid: Int, optionsGraph: OptionsGraph) {

    val offsetRT = Offset(offsetLT.x + size.width, offsetLT.y)
    val offsetRB = Offset(offsetLT.x + size.width, offsetLT.y + size.height)
    val offsetLB = Offset(offsetLT.x, offsetLT.y + size.height)
    val step = size.width/ optionsGraph.amountSections
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = optionsGraph.fontSize
        color = colorLine.hashCode()
        textAlign = android.graphics.Paint.Align.RIGHT
    }

    scope.drawLine(color = colorLine, start = offsetLT, end = offsetRT )
    scope.drawLine(color = colorLine, start = offsetLB, end = offsetRB )
    for (i in 0..optionsGraph.amountSections) {
        val x = offsetLT.x + i * step
        scope.drawLine(color = colorLine,
            start = Offset(x, offsetRT.y),
            end = Offset(x, offsetRB.y) )
        scope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                "${i * stepGrid}",
                x,
                offsetRB.y + optionsGraph.marginBottom,
                textPaint)
        }
    }
}

/**
 * Draws a bars for a vertical bar chart
 */
private fun drawVerticalBars(bars: List<Bar>, scope: DrawScope, offsetLT: Offset, size: Size,
                             maxLength: Int, colorLine: Color, colorRect: Color, sizeGrid: Int,
                             optionsGraph: OptionsGraph) {

    if (bars.isEmpty()) return

    val amountBars = min(bars.size, optionsGraph.maxAmountBars)
    val heightBar = (size.height - optionsGraph.betweenBars * (amountBars + 1)) / amountBars
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = optionsGraph.fontSize
        color = colorLine.hashCode()
        textAlign = android.graphics.Paint.Align.RIGHT
    }

    for (i in 0 until amountBars) {
        val widthBar = size.width * bars[i].value / sizeGrid
        val y = offsetLT.y + optionsGraph.betweenBars + i * (heightBar + optionsGraph.betweenBars)
        scope.drawRect(
            color = colorRect,
            topLeft = Offset(x = offsetLT.x, y = y),
            size = Size(width = widthBar, height = heightBar)
        )
        scope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                cutLabel(label = bars[i].label, maxLength = maxLength),
                offsetLT.x - optionsGraph.marginTop,
                y + (heightBar + optionsGraph.fontSize) / 2,
                textPaint)
        }
    }
}

/**
 * Label column width by pixel and character
 */
private data class Width(val byPixel: Float, val byChar: Int)

/**
 * Determination of the required width of the column of labels, but not more than the maximum
 */
private fun maxLabels(bars: List<Bar>, maxWidth : Float, textPaint: NativePaint) : Width {

    val widthG = textPaint.measureText("g")
    val limit: Int = (maxWidth / widthG).toInt()
    var max = 0

    for (bar in bars) {
        val len = bar.label.length
        if (len > limit) max = limit else if (len > max) max = len
    }
    return Width(byPixel = widthG * max, byChar = max)
}

/**
 * Truncates a string to its maximum length
 */
private fun cutLabel(label: String, maxLength: Int) : String {
    val len = label.length
    if (len <= maxLength) return label
    val cut = ((maxLength - 3) / 2)
    return label.replaceRange(cut until len - cut, "...")
}

/**
 * Size of the grid in units of a bar.value
 */
private fun sizeGrid(bars: List<Bar>, amountSections: Float) : Int {
    var max = amountSections
    for (bar in bars) { if(bar.value > max) { max = bar.value } }
    val dec = 10.0f.pow(ceil(log10(max + 0.5f).toDouble()).toInt() - 1)
    return (ceil(max / dec + 0.5f) * dec).toInt()
}