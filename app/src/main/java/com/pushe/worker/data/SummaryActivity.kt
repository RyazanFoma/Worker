package com.pushe.worker.data

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pushe.worker.R
import com.pushe.worker.data.Result.Success
import com.pushe.worker.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private lateinit var operation: Operation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val user = intent.extras?.get("user")
//        binding.fulNameView.text = user.toString()

        val barcode = intent.extras?.get("barcode").toString()
        val operationDataSource = OperationDataSource(this)
        operationDataSource.requestOperation(barcode)
        operationDataSource.observe(this, resultObserver)

    }
    private val resultObserver: Observer<Result<*>> = Observer { result ->

        if (result is Success<*>) {
            operation = result.data as Operation
            binding.operationName.text = operation.name
            binding.operationDate.text = operation.date
            binding.operationTime.text = operation.time.toString()
            binding.operationSum.text = operation.sum.toString()
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