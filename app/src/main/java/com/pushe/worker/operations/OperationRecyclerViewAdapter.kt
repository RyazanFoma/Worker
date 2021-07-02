package com.pushe.worker.operations

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pushe.worker.R

import com.pushe.worker.operations.model.Operations.Operation

/**
 * [RecyclerView.Adapter] that can display a [Operation].
 */
class OperationRecyclerViewAdapter(
        private val values: List<Operation>)
    : RecyclerView.Adapter<OperationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.operation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.nameView.text = item.name
        holder.durationView.text = item.duration.toString()
        holder.rateView.text = item.rate.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.id)
        val nameView: TextView = view.findViewById(R.id.name)
        val durationView: TextView = view.findViewById(R.id.duration)
        val rateView: TextView = view.findViewById(R.id.rate)

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }
}