package se.paradoxia.pxdemo.home

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import se.paradoxia.pxdemo.R

class HomeRecyclerViewAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent!!.rootView.context)
        when (viewType) {
            0 -> {
                val itemView = inflater.inflate(R.layout.card_profile_header, parent, false)
                viewHolder = CardProfileHeaderViewHolder(DataBindingUtil.bind(itemView))
            }
            1 -> {
                val itemView = inflater.inflate(R.layout.card_about_me, parent, false)
                viewHolder = CardAboutMeViewHolder(DataBindingUtil.bind(itemView))
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val typeId = getItemViewType(position)
        if (typeId == 0) {
            val cardProfileHeader = holder as CardProfileHeaderViewHolder
            cardProfileHeader.bind(items[position] as HomeViewModel.CardProfileHeader)
        } else if (typeId == 1) {
            val cardAboutMe = holder as CardAboutMeViewHolder
            cardAboutMe.bind(items[position] as HomeViewModel.CardAboutMe)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is HomeViewModel.CardProfileHeader -> 0
            items[position] is HomeViewModel.CardAboutMe -> 1
            else -> -1
        }
    }
}
