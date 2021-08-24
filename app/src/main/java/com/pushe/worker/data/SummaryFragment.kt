package com.pushe.worker.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app
import androidx.navigation.fragment.navArgs
import com.pushe.worker.operations.OperationActivity

import com.pushe.worker.operations.model.OperationDataSource
import com.pushe.worker.operations.model.Operation

class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private var operation: Operation? = null

    private val args: SummaryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val operationDataSource = OperationDataSource(requireContext())
        operationDataSource.requestOperation(args.barcode)
        operationDataSource.observe(this, resultObserver)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding.userName.text = "ФИО"
        binding.result.setOnClickListener { _ -> findNavController().navigate(
            SummaryFragmentDirections.actionSummaryToList(userId = args.userId)) }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val resultObserver: Observer<Result<*>> = Observer { result ->

        if (result is Success<*>) {
            operation = result.data as Operation
            operation!!.let {
                binding.operationName.text = it.name
                binding.operationDate.text = it.date
//                binding.operationTime.text = it.duration.toString()
//                binding.operationSum.text = it.rate.toString() }
                binding.operationTime.text = it.amount.toString()
                binding.operationSum.text = it.tarrif.toString() }

            binding.loading.visibility = View.GONE
            binding.result.setBackgroundResource(R.drawable.ic_bc_processing_result_ok)
            binding.result.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
            binding.result.setBackgroundResource(R.drawable.ic_bc_processing_result_closed)
            binding.result.visibility = View.VISIBLE
        }

    }

}