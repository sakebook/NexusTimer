package com.sakebook.android.nexustimer.presenter

import android.graphics.Color
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.sakebook.android.nexustimer.R

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class GridItemPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = TextView(parent.context)
        val res = parent.resources
        val width = res.getDimensionPixelSize(R.dimen.grid_item_width)
        val height = res.getDimensionPixelSize(R.dimen.grid_item_height)

        view.layoutParams = ViewGroup.LayoutParams(width, height)
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.primary_dark))
        view.setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        (viewHolder.view as TextView).text = item as String
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
    }
}