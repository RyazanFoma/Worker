package com.pushe.worker.operations

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.navArgs

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.pushe.worker.graph.BarGraph

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val args: SecondFragmentArgs by navArgs()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent { Hello(args.userId) }
        }
    }

    @Preview
    @Composable
    @ExperimentalMaterialApi
    fun Hello(userId: String = "123456") = MaterialTheme {
        var stateTabs by remember { mutableStateOf(1) }
        val titleTabs = listOf("Неделя", "Месяц", "Год")
        Column() {
            Row(modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 8.dp)) {
                Text(text = "99-99 ММММ ГГГГ г. = 99`999.99 р.",
                    modifier = Modifier.padding(end = 16.dp))
            }
            TabRow(
                selectedTabIndex = stateTabs,
                backgroundColor = MaterialTheme.colors.background,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                titleTabs.forEachIndexed { indexTab, title ->
                    Tab(
                        content = { Text(title) },
                        selected = stateTabs == indexTab,
                        onClick = { stateTabs = indexTab }
                    )
                }
            }
            when (LocalConfiguration.current.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    BarGraph(
                        bars = listOf(
                            BarGraph.Bar(label = "1", value = 10000.1f),
                            BarGraph.Bar(label = "2", value = 20000.1f),
                            BarGraph.Bar(label = "3", value = 0f),
                            BarGraph.Bar(label = "4", value = 30000.1f),
                            BarGraph.Bar(label = "5", value = 20000.1f),
                            BarGraph.Bar(label = "6", value = 0f),
                            BarGraph.Bar(label = "7", value = 0f),
                            BarGraph.Bar(label = "8", value = 10000.1f),
                            BarGraph.Bar(label = "9", value = 20000.2f),
                            BarGraph.Bar(label = "10", value = 0f),
                            BarGraph.Bar(label = "11", value = 10000.1f),
                            BarGraph.Bar(label = "12", value = 20000.2f),
                            BarGraph.Bar(label = "13", value = 0f),
                            BarGraph.Bar(label = "14", value = 0f),
                            BarGraph.Bar(label = "15", value = 20000.2f),
                            BarGraph.Bar(label = "16", value = 10000.1f),
                            BarGraph.Bar(label = "17", value = 0f),
                            BarGraph.Bar(label = "18", value = 20000.2f),
                            BarGraph.Bar(label = "19", value = 10000.1f),
                            BarGraph.Bar(label = "20", value = 0f),
                            BarGraph.Bar(label = "21", value = 0f),
                            BarGraph.Bar(label = "22", value = 10000.1f),
                            BarGraph.Bar(label = "23", value = 10000.1f),
                            BarGraph.Bar(label = "24", value = 0f),
                            BarGraph.Bar(label = "25", value = 10000.1f),
                            BarGraph.Bar(label = "26", value = 10000.1f),
                            BarGraph.Bar(label = "27", value = 0f),
                            BarGraph.Bar(label = "28", value = 0f),
                            BarGraph.Bar(label = "29", value = 10000.1f),
                            BarGraph.Bar(label = "30", value = 20000.2f),
                            BarGraph.Bar(label = "31", value = 200f)
                        )
                    ).Horizontal(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .background(color = MaterialTheme.colors.surface),
                        colorLine = MaterialTheme.colors.onSurface,
                        colorRect = MaterialTheme.colors.primaryVariant
                    )
                }
                else -> {
                    BarGraph(
                        bars = listOf(
                            BarGraph.Bar(label = "Обклейка каркаса Дюваль ", value = 30098f),
                            BarGraph.Bar(label = "Набивка подушек Дюваль", value = 30000f),
                            BarGraph.Bar(label = "Набивка подушек Дюваль", value = 20550f),
                            BarGraph.Bar(label = "Обивка кресла Дюваль", value = 20098f),
                            BarGraph.Bar(label = "Обивка кресла Дюваль", value = 20000f),
                            BarGraph.Bar(label = "Обклейка каркаса Дюваль", value = 10598f),
                            BarGraph.Bar(label = "Обивка дивана Дюваль", value = 10100f),
                            BarGraph.Bar(label = "Обивка дивана Дюваль", value = 10000f),
                        )
                    ).Vertical(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .background(color = MaterialTheme.colors.surface),
                        colorLine = MaterialTheme.colors.onSurface,
                        colorRect = MaterialTheme.colors.primaryVariant
                    )
                }
            }
        }
    }
}
