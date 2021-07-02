package com.pushe.worker.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pushe.worker.R
import com.pushe.worker.databinding.OperationListBinding
import com.pushe.worker.operations.model.Operations
import com.pushe.worker.ui.login.LoginActivity.USER_ID
import com.pushe.worker.ui.login.LoginActivity.USER_NAME

/**
 * A fragment representing a list of Items.
 */
class OperationFragment : Fragment(R.layout.operation_list) {

    private var _binding: OperationListBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null
    private var userName: String? = null
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userId = it.getString(USER_ID)
            userName = it.getString(USER_NAME)
        }
        columnCount = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = OperationListBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set the adapter
        with(view) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = OperationRecyclerViewAdapter(Operations.ITEMS)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(userId: String, userName: String) =
                OperationFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                        putString(USER_NAME, userName)
                    }
                }
    }
}