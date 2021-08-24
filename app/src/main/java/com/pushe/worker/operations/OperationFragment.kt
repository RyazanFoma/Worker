package com.pushe.worker.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pushe.worker.R
import com.pushe.worker.databinding.OperationListBinding
import com.pushe.worker.ui.login.LoginActivity.USER_ID
import com.pushe.worker.ui.login.LoginActivity.USER_NAME
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class OperationFragment : Fragment() {

    private var _binding: OperationListBinding? = null
    private val binding get() = _binding!!
    private var columnCount = 1
    private val args: OperationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        columnCount = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = OperationListBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = OperationViewModelFactory(this.context!!, args.userId, "29.07.2021")
        val viewModel by viewModels<OperationViewModel> { viewModelFactory }
        val pagingAdapter = OperationRecyclerViewAdapter(OperationComparator)

        // Set the adapter
        with(view) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = pagingAdapter
        }

        // Activities can use lifecycleScope directly, but Fragments should instead use
        // viewLifecycleOwner.lifecycleScope.
        lifecycleScope.launch {
            viewModel.allOperations.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}