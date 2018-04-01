package se.paradoxia.pxdemo.util

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import timber.log.Timber

class ViewTypeMapper(
    dataHolderClass: Class<*>, @LayoutRes private val resLayout: Int,
    private val viewHolderClass: Class<*>
) {

    val viewType = dataHolderClass.canonicalName.hashCode()

    fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup?): BindingTemplateViewHolder {
        val itemView = inflater.inflate(resLayout, parent, false)
        val constructor = viewHolderClass.constructors[0]
        val instance = constructor.newInstance(DataBindingUtil.bind(itemView))
        return instance as BindingTemplateViewHolder
    }

}

class FlexibleRecyclerViewAdapter(
    private val viewTypeMappers: List<ViewTypeMapper>,
    private val items: List<Any>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {

        val inflater = LayoutInflater.from(parent!!.context)
        context = parent.context

        //val inflater = LayoutInflater.from(parent!!.rootView.context)
        val viewTypeMapper = viewTypeMappers.find { it.viewType == viewType }
        return if (viewTypeMapper != null) {
            viewTypeMapper.getViewHolder(inflater, parent)
        } else {
            Timber.w(RuntimeException("Unknown viewType $viewType"))
            null
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BindingTemplateViewHolder).bind(items[position], context as LifecycleOwner)
    }

    override fun getItemViewType(position: Int): Int {
        return classToViewType(this.items[position].javaClass)
    }

    private fun classToViewType(clazz: Class<*>): Int {
        return clazz.canonicalName.hashCode()
    }

}
