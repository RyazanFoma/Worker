package com.pushe.worker.totals

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.navArgs

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TotalFragment : Fragment() {
    private val args: TotalFragmentArgs by navArgs()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WorkerTheme {
                    TotalScreen(this.context, args.userId)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun TotalScreen(context: Context, userId: String) {
    val orientation = LocalConfiguration.current.orientation
    val modelView: TotalsViewModel = viewModel(
        factory = TotalsViewModelFactory(context, userId)
    )

    modelView.setAnalytics(analyticsNew = when(orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> TotalsViewModel.Analytics.TIME
            else -> TotalsViewModel.Analytics.TYPE //Configuration.ORIENTATION_PORTRAIT
        }
    )

    TotalScreen(
        status = modelView.status,
        orientation = orientation,
        bars = modelView.bars,
        title = modelView.title,
        error = modelView.error,
        startTab = modelView.period.periodSize.ordinal,
        onSelectTab = modelView::changePeriodSize,
        onLeftShift = modelView::nextPeriod,
        onRightShift = modelView::previousPeriod,
        onRefresh = modelView::loadTotals
    )
}

