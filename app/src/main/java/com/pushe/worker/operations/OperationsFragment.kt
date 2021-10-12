package com.pushe.worker.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pushe.worker.totals.WorkerTheme

/**
 * A fragment representing a list of Items.
 */
class OperationsFragment : Fragment() {

    private val args: OperationsFragmentArgs by navArgs()

    @ExperimentalFoundationApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewModel by viewModels<OperationsViewModel> {
            OperationsViewModelFactory(this.context!!, args.userId)
        }
//        val pagingAdapter = OperationRecyclerViewAdapter(OperationComparator)


        // Activities can use lifecycleScope directly, but Fragments should instead use
        // viewLifecycleOwner.lifecycleScope.
//        lifecycleScope.launch {
//            viewModel.allOperations.collectLatest { pagingData ->
//                pagingAdapter.submitData(pagingData)
//            }
//        }
//        return view

        return ComposeView(requireContext()).apply {
            setContent {
                WorkerTheme {
                    OperationsScreen(viewModel = viewModel)
                }
            }
        }
    }

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