package se.paradoxia.pxdemo.home

import android.support.v7.widget.RecyclerView
import se.paradoxia.pxdemo.databinding.CardAboutMeBinding

/**
 * Created by mikael on 2018-01-25.
 */
class CardAboutMeViewHolder(private val binding: CardAboutMeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeViewModel.CardAboutMe) {
        binding.cardAboutMe = item
        binding.executePendingBindings()
    }

}

