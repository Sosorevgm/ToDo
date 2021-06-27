package com.sosorevgm.todo.features.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sosorevgm.todo.R
import com.sosorevgm.todo.databinding.HolderTaskBinding
import com.sosorevgm.todo.domain.presentation.AbstractHolder
import com.sosorevgm.todo.models.TaskModel

class TaskRVAdapter(
    private val listener: IListener
) : ListAdapter<TaskViewData, AbstractHolder<out TaskViewData>>(TaskItemCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TASK = 1
        private const val VIEW_TYPE_NEW_TASK = 2
    }

    interface IListener {
        fun onTaskCheckboxClicked(task: TaskModel)
        fun onTaskClicked(task: TaskModel)
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is TaskViewData.Header -> VIEW_TYPE_HEADER
        is TaskViewData.Task -> VIEW_TYPE_TASK
        is TaskViewData.NewTask -> VIEW_TYPE_NEW_TASK
        else -> throw IllegalArgumentException("Unknown item type")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractHolder<out TaskViewData> = when (viewType) {
        VIEW_TYPE_HEADER -> TaskHeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.holder_task_header, parent, false)
        )
        VIEW_TYPE_TASK -> TaskViewHolder(
            HolderTaskBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.holder_task, parent, false)
            ),
            listener
        )
        VIEW_TYPE_NEW_TASK -> {
            NewTaskViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.holder_new_task, parent, false)
            )
        }
        else -> throw IllegalArgumentException("Unknown item type")
    }

    override fun onBindViewHolder(holder: AbstractHolder<out TaskViewData>, position: Int) {
        holder.bindHolder(getItem(position))
    }
}

class TaskItemCallback : DiffUtil.ItemCallback<TaskViewData>() {
    override fun areItemsTheSame(oldItem: TaskViewData, newItem: TaskViewData): Boolean {
        if (oldItem is TaskViewData.Task && newItem is TaskViewData.Task) {
            return oldItem.description == newItem.description
        }
        return true
    }

    override fun areContentsTheSame(oldItem: TaskViewData, newItem: TaskViewData): Boolean {
        if (oldItem is TaskViewData.Task && newItem is TaskViewData.Task) {
            return oldItem == newItem
        }
        return true
    }
}