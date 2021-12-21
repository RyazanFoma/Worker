package com.pushe.worker.operations.ui

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.pushe.worker.operations.model.PeriodSize
import com.pushe.worker.utils.Status
import com.pushe.worker.utils.Bar
import com.pushe.worker.utils.ErrorMessage
import com.pushe.worker.utils.Horizontal
import com.pushe.worker.utils.Vertical
import kotlinx.coroutines.launch

enum class SwipeDirection { Left, Initial, Right, }

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TotalsScreen(
    status: Status,
    orientation: Int,
    bars: List<Bar>,
    title: String,
    error: String,
    startTab: PeriodSize,
    onSelectTab: (PeriodSize) -> Unit,
    onLeftShift: () -> Unit,
    onRightShift: () -> Unit,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Title(title)
        TabRow( startTab = startTab, onSelectTab = onSelectTab)
        SwipeableBox(onLeftShift = onLeftShift, onRightShift = onRightShift) {
            if (status == Status.SUCCESS)
                BarChart(bars = bars, orientation = orientation)
            else
                BarChart(orientation = orientation) //The PlaceHolder
            if (status == Status.LOADING)
                CircularProgressIndicator(color = MaterialTheme.colors.secondary)
            if (status == Status.ERROR)
                ErrorMessage(error = error, onRefresh = onRefresh)
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun SwipeableBox(
    onLeftShift: () -> Unit,
    onRightShift: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    var width by remember { mutableStateOf(1f) }
    val swipeableState = rememberSwipeableState(SwipeDirection.Initial)
    val scope = rememberCoroutineScope()
    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeableState.currentValue) {
                    SwipeDirection.Right -> onRightShift()
                    SwipeDirection.Left -> onLeftShift()
                    else -> return@onDispose
                }
                scope.launch { swipeableState.snapTo(SwipeDirection.Initial) }
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { width = it.width.toFloat() }
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to SwipeDirection.Left,
                    width / 2 to SwipeDirection.Initial,
                    width to SwipeDirection.Right,
                ),
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            ),
        content = content
    )
}

@Composable
private fun Title(title: String) {
    Text(text = title, modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun TabRow(startTab: PeriodSize, onSelectTab: (PeriodSize) -> Unit) {
    var state by remember { mutableStateOf(startTab) }

    TabRow(
        selectedTabIndex = state.ordinal,
        modifier = Modifier.padding(4.dp),
        backgroundColor = MaterialTheme.colors.background
    ) {
        PeriodSize.values().forEach {
            Tab(
                text = { Text(it.value) },
                selected = state == it,
                onClick = {
                    onSelectTab(it)
                    state = it
                }
            )
        }
    }
}

@Composable
private fun BarChart(bars: List<Bar> = listOf(), orientation: Int) {
    val colorLine = MaterialTheme.colors.onSurface
    val colorRect = MaterialTheme.colors.primaryVariant
    val modifier = Modifier
        .fillMaxSize()
        .padding(4.dp)

    when (orientation) {
        ORIENTATION_LANDSCAPE ->
            Horizontal(
                bars = bars,
                modifier = modifier,
                colorLine = colorLine,
                colorRect = colorRect
            )
        ORIENTATION_PORTRAIT ->
            Vertical(
                bars = bars,
                modifier = modifier,
                colorLine = colorLine,
                colorRect = colorRect
            )
    }
}