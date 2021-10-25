package com.pushe.worker.totals

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pushe.worker.utils.Bar
import com.pushe.worker.totals.TotalsViewModel.Status
import com.pushe.worker.utils.ErrorMessage
import com.pushe.worker.utils.Horizontal
import com.pushe.worker.utils.Vertical

@Composable
fun TotalsScreen(
    status: TotalsViewModel.Status,
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
        Surface(modifier = Modifier.fillMaxSize()) {
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

@Composable
private fun TabRow(startTab: PeriodSize, onSelectTab: (PeriodSize) -> Unit) {
    var state by remember { mutableStateOf(startTab) }

    TabRow(
        selectedTabIndex = state.ordinal,
        modifier = Modifier.padding(4.dp)
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

    when (orientation) {
        ORIENTATION_LANDSCAPE ->
            Horizontal(
                bars = bars,
                modifier = Modifier.fillMaxSize(),
                colorLine = colorLine,
                colorRect = colorRect
            )
        ORIENTATION_PORTRAIT ->
            Vertical(
                bars = bars,
                modifier = Modifier.fillMaxSize(),
                colorLine = colorLine,
                colorRect = colorRect
            )
    }
}

@Composable
private fun Title(title: String) {
    Text(text = title, modifier = Modifier.padding(8.dp))
}