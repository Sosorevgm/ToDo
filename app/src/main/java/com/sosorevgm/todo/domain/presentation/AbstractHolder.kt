package com.sosorevgm.todo.domain.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class AbstractHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @JvmField
    protected var mItem: T? = null

    fun bindHolder(item: Any?) {
        if (item != null) {
            val temp: T = item as T
            mItem = temp
            bind(temp)
        }
    }

    open fun bind(item: T) {
    }
}