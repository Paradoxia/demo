package se.paradoxia.pxdemo.util

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by mikael on 2018-01-25.
 */
abstract class BindingTemplateViewHolder(private val variableId: Int, private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Any?) {
        binding.setVariable(variableId, item)
        binding.executePendingBindings()
    }

}

