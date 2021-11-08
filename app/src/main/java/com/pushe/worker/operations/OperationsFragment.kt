package com.pushe.worker.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pushe.worker.theme.WorkerTheme

/**
 * A fragment representing a list of Items.
 */
class OperationsFragment : Fragment() {

    private val args: OperationsFragmentArgs by navArgs()

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewModel by viewModels<OperationsViewModel> {
            OperationsViewModelFactory(this.context!!, args.userId)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                WorkerTheme {
                    OperationsScreen(viewModel = viewModel)
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @Composable
    private fun OperationsScreen(
        viewModel: OperationsViewModel
    ) {
        OperationsScreen(
            operationsFlow = viewModel.operationsFlow,
            isRefreshing = false
        )
    }

 }