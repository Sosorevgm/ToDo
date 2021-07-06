package com.sosorevgm.todo.features.tasks.recycler

import android.graphics.Paint
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import com.sosorevgm.todo.R
import com.sosorevgm.todo.databinding.HolderTaskBinding
import com.sosorevgm.todo.domain.presentation.AbstractHolder
import com.sosorevgm.todo.extensions.getTaskDate
import com.sosorevgm.todo.models.TaskPriority

class TaskViewHolder(
    private val binding: HolderTaskBinding,
    private val listener: TaskRVAdapter.IListener
) : AbstractHolder<TaskViewData.Task>(binding.root) {


    override fun bind(item: TaskViewData.Task) {
        if (item.done) {
            binding.ivTaskCheckbox.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.icon_checkbox_green
                )
            )
        } else {
            val currentTime = System.currentTimeMillis() / 1000
            if (item.deadline != 0L && item.deadline - currentTime < 0) {
                binding.ivTaskCheckbox.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.icon_checkbox_red
                    )
                )
            } else {
                binding.ivTaskCheckbox.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.icon_checkbox_gray
                    )
                )
            }
        }

        if (item.priority == TaskPriority.DEFAULT) {
            binding.tvTaskDescription.text = item.text
        } else {
            val strBuilder = SpannableStringBuilder()
            if (item.priority == TaskPriority.LOW) {
                val arrowDown = SpannableString("↓")
                arrowDown.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.gray
                        )
                    ), 0, arrowDown.length, 0
                )
                strBuilder.bold { append(arrowDown) }
                strBuilder.append(" ")
                strBuilder.append(item.text)
            } else if (item.priority == TaskPriority.HIGH) {
                val exclamation = SpannableString("!!")
                exclamation.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.red
                        )
                    ), 0, exclamation.length, 0
                )
                strBuilder.bold { append(exclamation) }
                strBuilder.append(" ")
                strBuilder.append(item.text)
            }
            binding.tvTaskDescription.setText(strBuilder, TextView.BufferType.SPANNABLE)
        }

        if (item.done) {
            binding.tvTaskDescription.setTextColor(
                ContextCompat.getColor(itemView.context, R.color.gray)
            )
            binding.tvTaskDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.tvTaskDescription.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.text_color
                )
            )
            binding.tvTaskDescription.paintFlags = 0
        }

        if (item.deadline != 0L) {
            binding.tvTaskDate.visibility = View.VISIBLE
            binding.tvTaskDate.text = getTaskDate(item.deadline)
        } else {
            binding.tvTaskDate.visibility = View.GONE
        }

        binding.btnTaskCheck.setOnClickListener {
            listener.onTaskCheckboxClicked(item.toTaskModel())
        }

        binding.taskHolderLayout.setOnClickListener {
            listener.onTaskClicked(item.toTaskModel())
        }
    }
}