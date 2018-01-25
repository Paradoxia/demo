package se.paradoxia.pxdemo.home

import android.support.v7.widget.RecyclerView
import se.paradoxia.pxdemo.databinding.CardProfileHeaderBinding

/**
 * Created by mikael on 2018-01-25.
 */
class CardProfileHeaderViewHolder(private val binding: CardProfileHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeViewModel.CardProfilerHeader) {
        binding.cardProfileHeader = item
        binding.executePendingBindings()
    }

}

