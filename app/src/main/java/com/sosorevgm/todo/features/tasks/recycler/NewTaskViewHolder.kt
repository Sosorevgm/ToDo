package com.sosorevgm.todo.features.tasks.recycler

import com.sosorevgm.todo.databinding.HolderNewTaskBinding
import com.sosorevgm.todo.domain.presentation.AbstractHolder

class NewTaskViewHolder(
    private val binding: HolderNewTaskBinding,
    private val listener: IListener
) : AbstractHolder<TaskViewData.NewTask>(binding.root) {

    interface IListener {
        fun onNewTaskClick()
    }

    override fun bind(item: TaskViewData.NewTask) {
        super.bind(item)
        binding.root.setOnClickListener {
            listener.onNewTaskClick()
        }
    }
}