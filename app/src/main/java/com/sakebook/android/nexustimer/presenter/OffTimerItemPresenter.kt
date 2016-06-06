package com.sakebook.android.nexustimer.presenter

import android.graphics.Color
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.sakebook.android.nexustimer.R
import com.sakebook.android.nexustimer.model.OffTimer

/**
 * Created by sakemotoshinya on 16/06/05.
 */
class OffTimerItemPresenter() : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = TextView(parent.context)
        val res = parent.resources
        val width = res.getDimensionPixelSize(R.dimen.card_width)
        val height = res.getDimensionPixelSize(R.dimen.card_height)

        view.layoutParams = ViewGroup.LayoutParams(width, height)
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.primary_dark))
        view.setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER
        view.textSize = res.getDimension(R.dimen.font_content_title_big) / res.displayMetrics.scaledDensity
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        (viewHolder.view as TextView).text = "${(item as OffTimer).minute}m"
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
    }
}