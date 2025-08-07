package com.example.recipeapp.homeflow.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeRecyclerViewItemDecoration(
    private val startOffset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.left = startOffset
        } else {
            outRect.left = 0
        }
    }
}
