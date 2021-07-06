package com.sosorevgm.todo.features.tasks.recycler

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sosorevgm.todo.R

class TaskTouchHelper(
    private val listener: IListener
) : ItemTouchHelper.Callback() {

    interface IListener {
        fun taskDoneSwipe(position: Int)
        fun taskDeleteSwipe(position: Int)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is TaskHeaderViewHolder) return 0
        if (viewHolder is NewTaskViewHolder) return 0
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.START -> listener.taskDeleteSwipe(viewHolder.adapterPosition)
            ItemTouchHelper.END -> listener.taskDoneSwipe(viewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView: View = viewHolder.itemView
            val p = Paint()
            if (dX > 0) {
                p.color = ContextCompat.getColor(itemView.context, R.color.green)

                c.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    dX + 32f,
                    itemView.bottom.toFloat(),
                    p
                )

                val icon =
                    BitmapFactory.decodeResource(
                        itemView.context.resources,
                        R.drawable.icon_check_mark_png
                    )

                c.drawBitmap(
                    icon,
                    itemView.left.toFloat() + 60f,
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                    p
                )
            } else {
                p.color = ContextCompat.getColor(itemView.context, R.color.red)

                c.drawRect(
                    itemView.right.toFloat() + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    p
                )

                val icon =
                    BitmapFactory.decodeResource(
                        itemView.context.resources,
                        R.drawable.icon_delete_png
                    )

                c.drawBitmap(
                    icon,
                    itemView.right.toFloat() - icon.width - 60f,
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                    p
                )
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}