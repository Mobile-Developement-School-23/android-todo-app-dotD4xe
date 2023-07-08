package com.example.todolist.presentation.todoList.recyclerView

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.todolist.R

/**
 * Helper class for painting swipe backgrounds and icons during item swiping in a RecyclerView.
 */
class SwipeBackgroundHelper {
    companion object {
        private const val OFFSET_PX = 20

        @JvmStatic
        fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float) {
            val drawCommand = createDrawCommand(viewItem, iconResId)
            paintDrawCommand(drawCommand, canvas, dX, viewItem)
        }

        private fun createDrawCommand(viewItem: View, iconResId: Int): DrawCommand {
            val context = viewItem.context
            val icon = ContextCompat.getDrawable(context, iconResId)?.mutate()
            val backgroundColor = ContextCompat.getColor(context, R.color.red)
            return DrawCommand(icon, backgroundColor)
        }

        private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
            drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
            drawIcon(canvas, viewItem, dX, drawCommand.icon)
        }

        private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float, icon: Drawable?) {
            icon?.let {
                val density = viewItem.context.resources.displayMetrics.density
                val paddingStartPx = (OFFSET_PX * density).toInt()
                val topMargin = (viewItem.height - it.intrinsicHeight) / 2
                val iconWidth = it.intrinsicWidth
                val iconHeight = it.intrinsicHeight
                val left = viewItem.right + dX.toInt() + OFFSET_PX + paddingStartPx
                val top = viewItem.top + topMargin
                val right = left + iconWidth
                val bottom = top + iconHeight

                it.setBounds(left, top, right, bottom)

                it.draw(canvas)
            }
        }

        private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, color: Int) {
            val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            backgroundPaint.color = color
            val backgroundRectangle = RectF(
                viewItem.right.toFloat() + dX,
                viewItem.top.toFloat(),
                viewItem.right.toFloat(),
                viewItem.bottom.toFloat()
            )
            canvas.drawRect(backgroundRectangle, backgroundPaint)
        }
    }

    private data class DrawCommand(val icon: Drawable?, val backgroundColor: Int)
}
