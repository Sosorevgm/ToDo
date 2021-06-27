package com.sosorevgm.todo.domain.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import com.sosorevgm.todo.R
import com.sosorevgm.todo.databinding.WidgetCollapsingToolbarBinding

class CollapsingToolbarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    private val binding: WidgetCollapsingToolbarBinding by lazy {
        WidgetCollapsingToolbarBinding.bind(this)
    }

    fun setTasks(tasks: Int) {
        val string = "${context.getString(R.string.tasks_done)} $tasks"
        binding.tvTasksDone.text = string
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }
}