package com.pushe.worker.operations

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pushe.worker.databinding.OperationItemBinding
import com.pushe.worker.operations.model.Operation


/**
 * [RecyclerView.Adapter] that can display a [Operation].
 */
class OperationRecyclerViewAdapter(diffCallback: DiffUtil.ItemCallback<OperationListItem>)
    : PagingDataAdapter<OperationListItem, OperationRecyclerViewAdapter.ViewHolder>(diffCallback) {

    private var _binding: OperationItemBinding? = null
    private val bind get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = OperationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.title.text = this?.title ?: "no title"
                if (this?.subtitle != null) {
                    binding.subtitle.text = this.subtitle
                    binding.subtitle.visibility = View.VISIBLE
                } else
                    binding.subtitle.visibility = View.GONE
            }
        }
    }

    inner class ViewHolder(val binding: OperationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

object OperationComparator : DiffUtil.ItemCallback<OperationListItem>() {
    override fun areItemsTheSame(oldItem: OperationListItem, newItem: OperationListItem): Boolean {
        // Id is unique.
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: OperationListItem, newItem: OperationListItem): Boolean {
        return oldItem == newItem
    }
}