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
import com.pushe.worker.theme.WorkerTheme

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TotalsFragment : Fragment() {
    private val args: TotalsFragmentArgs by navArgs()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WorkerTheme {
                    TotalsScreen(this.context, args.userId)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun TotalsScreen(context: Context, userId: String) {
    val orientation = LocalConfiguration.current.orientation
    val viewModel: TotalsViewModel = viewModel(
        factory = TotalsViewModelFactory(context, userId)
    )

    viewModel.setAnalytics(analyticsNew = when(orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> TotalsViewModel.Analytics.TIME
            else -> TotalsViewModel.Analytics.TYPE //Configuration.ORIENTATION_PORTRAIT
        }
    )

    TotalsScreen(
        status = viewModel.status,
        orientation = orientation,
        bars = viewModel.bars,
        title = viewModel.title,
        error = viewModel.error,
        startTab = viewModel.period.periodSize.ordinal,
        onSelectTab = viewModel::changePeriodSize,
        onLeftShift = viewModel::nextPeriod,
        onRightShift = viewModel::previousPeriod,
        onRefresh = viewModel::loadTotals
    )
}

