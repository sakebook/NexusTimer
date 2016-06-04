package com.sakebook.android.nexustimer

import android.graphics.Color
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class TvFragment: BrowseFragment() {

    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private val GRID_ITEM_WIDTH = 200
    private val GRID_ITEM_HEIGHT = 200

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        loadRows()
        setupEventListeners()
    }

    private fun setupUIElements() {
        title = resources.getString(R.string.app_name)
        brandColor = ContextCompat.getColor(this.activity, R.color.primary)
        searchAffordanceColor = ContextCompat.getColor(this.activity, R.color.accent)
    }

    private fun loadRows() {
        val headerItem0 = HeaderItem(0, "Good Morning")
        val gridPresenter = GridItemPresenter()
        val gridRowAdapter0 = ArrayObjectAdapter(gridPresenter)
        gridRowAdapter0.add("ON")
        gridRowAdapter0.add("SETTING")
        gridRowAdapter0.add("DELETE")
        rowsAdapter.add(ListRow(headerItem0, gridRowAdapter0))

        val headerItem1 = HeaderItem(0, "Good Night")
        val gridRowAdapter1 = ArrayObjectAdapter(gridPresenter)
        gridRowAdapter1.add("ON")
        gridRowAdapter1.add("SETTING")
        gridRowAdapter1.add("DELETE")
        rowsAdapter.add(ListRow(headerItem1, gridRowAdapter1))
        adapter = rowsAdapter
    }

    private fun setupEventListeners() {

        setOnSearchClickedListener { Toast.makeText(activity, "Implement your own in-app search", Toast.LENGTH_LONG).show() }

//        onItemViewClickedListener = ItemViewClickedListener()
//        onItemViewSelectedListener = ItemViewSelectedListener()

    }


    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.primary_dark))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        }
    }

}