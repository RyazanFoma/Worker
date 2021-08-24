package com.pushe.worker.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pushe.worker.operations.model.OperationDataSource

class SummaryFragment : Fragment() {

    private lateinit var operationDataSource: OperationDataSource;

    private val args: SummaryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        operationDataSource = OperationDataSource(requireContext())
        operationDataSource.requestOperation(args.barcode)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply { setContent { ResultScreen(operationDataSource) } }

    }


    @Composable
    fun ResultScreen(operationDataSource: OperationDataSource) {

        Text("Hello!!!")
//        val result by operationDataSource.observeAsState()
//
//        ShowOperationResult(result = result as Result<Any>?)

    }
}
