package com.pushe.worker.data

import android.graphics.drawable.shapes.OvalShape
import android.media.VolumeShaper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.pushe.worker.R
import com.pushe.worker.data.Result.Success
import com.pushe.worker.databinding.FragmentSummaryBinding
import com.pushe.worker.operations.OperationActivity

import com.pushe.worker.operations.model.OperationDataSource
import com.pushe.worker.ui.login.LoginActivity
import com.pushe.worker.operations.model.Operation
import com.pushe.worker.operations.ui.theme.ui.theme.ComposeTheme
import com.pushe.worker.operations.ui.theme.ui.theme.Typography

class SummaryFragment : Fragment(R.layout.operation_list) {

    //private var _binding: FragmentSummaryBinding? = null
  //  private val binding get() = _binding!!
    private var userId: String? = null
    private var userName: String? = null
    private var operation: Operation? = Operation()
    private var barcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userId = it.getString(LoginActivity.USER_ID)
            userName = it.getString(LoginActivity.USER_NAME)
            barcode = it.getString(OperationActivity.BARCODE)
        }

        val operationDataSource = OperationDataSource(requireContext())
        operationDataSource.requestOperation(barcode)
        operationDataSource.observe(this, resultObserver)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
//        binding.userName.text = userId
//        return binding.root
        return ComposeView(requireContext()).apply { setContent { ShowOperationResult()} }
    }

    override fun onDestroyView() {
        super.onDestroyView()
  //      _binding = null
    }

    private val resultObserver: Observer<Result<*>> = Observer { result ->

//        if (result is Success<*>) {
//            operation = result.data as Operation
//            operation!!.let {
//                binding.operationName.text = it.name
//                binding.operationDate.text = it.date
//                binding.operationTime.text = it.amount.toString()
//                binding.operationSum.text = it.tarrif.toString() }
//
//            binding.loading.visibility = View.GONE
//            binding.result.setBackgroundResource(R.drawable.ic_bc_processing_result_ok)
//            binding.result.visibility = View.VISIBLE
//        } else {
//            binding.loading.visibility = View.GONE
//            binding.result.setBackgroundResource(R.drawable.ic_bc_processing_result_closed)
//            binding.result.visibility = View.VISIBLE
//        }

    }

    @Preview
    @Composable
    fun ShowOperationResult() = ComposeTheme() {

        Column() {
            OperationText(text = "Операция: ${operation?.name}")
            OperationText(text = "Дата: ${operation?.date}")
            OperationText(text = "Количество: ${operation?.amount}")
            OperationText(text = "Сумма: ${operation?.sum}")
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "TEST")
            }
        }



//            Column(
//                Modifier.bac(id = R.drawable.ic_bc_processing_result_ok),
//                contentDescription = null,
//                Modifier
//                    .width(220.dp)
//                    .height(70.dp)
//                    .clip(CircleShape)
//            )



    }

    @Composable
    fun OperationText(text: String) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
            Text(
                text = text,
                Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.h4
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}