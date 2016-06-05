package com.sakebook.android.nexustimer.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.sakebook.android.nexustimer.R
import com.sakebook.android.nexustimer.presenter.GridItemPresenter
import com.sakebook.android.nexustimer.presenter.ItemViewClickedListener
import com.sakebook.android.nexustimer.presenter.OffTimerItemPresenter
import java.util.*

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class TvFragment: BrowseFragment() {

    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    val REQUEST_CODE = 100

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
        rowsAdapter.add(createDefaultTimer())
        val title0 = "Good Morning"
        val headerItem0 = HeaderItem(title0.hashCode().toLong(), title0)
        val gridPresenter = GridItemPresenter()
        val gridRowAdapter0 = ArrayObjectAdapter(gridPresenter)
        gridRowAdapter0.add("ON")
        gridRowAdapter0.add("SETTING")
        gridRowAdapter0.add("DELETE")
        rowsAdapter.add(ListRow(headerItem0, gridRowAdapter0))

        val title1 = "Good Night"
        val headerItem1 = HeaderItem(title1.hashCode().toLong(), title1)
        val gridRowAdapter1 = ArrayObjectAdapter(gridPresenter)
        gridRowAdapter1.add("ON")
        gridRowAdapter1.add("SETTING")
        gridRowAdapter1.add("DELETE")
        rowsAdapter.add(ListRow(headerItem1, gridRowAdapter1))
        adapter = rowsAdapter
    }

    private fun createDefaultTimer(): ListRow {
        val title = resources.getString(R.string.off_timer_title)
        val headerItem = HeaderItem(title.hashCode().toLong(), title)
        val gridPresenter = OffTimerItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(gridPresenter)
        gridRowAdapter.add("5")
        gridRowAdapter.add("15")
        gridRowAdapter.add("30")
        gridRowAdapter.add("60")
        gridRowAdapter.add("90")
        gridRowAdapter.add("120")
        return ListRow(headerItem, gridRowAdapter)
    }

    private fun setupEventListeners() {

        setOnSearchClickedListener {
            val title = UUID.randomUUID().toString()
            val headerItem = HeaderItem(title.hashCode().toLong(), title)
            val gridPresenter = GridItemPresenter()
            val gridRowAdapter = ArrayObjectAdapter(gridPresenter)
            gridRowAdapter.add("ON")
            gridRowAdapter.add("SETTING")
            gridRowAdapter.add("DELETE")
            rowsAdapter.add(ListRow(headerItem, gridRowAdapter))

//            AlertDialog.Builder(activity)
//                .setTitle("create")
//                .setMessage("message")
//                    .setView(R.layout.dialog_layout)
//                .setPositiveButton("ok") { dialog, which ->
//                }
//                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener(this)
//        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(activity, "onActivityResult: ${requestCode}, ${resultCode}", Toast.LENGTH_LONG).show()
        if (REQUEST_CODE != requestCode) {
            return
        }
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        data?.let {
            val id = it.extras.getLong(SimpleDialogActivity.ARG_ID)
            Toast.makeText(activity, "onActivityResult delete: ${id}", Toast.LENGTH_LONG).show()
            loop@ for (i in 0..rowsAdapter.size()) {
                val item = rowsAdapter.get(i) as ListRow
                when (id) {
                    item.id -> {
                        rowsAdapter.remove(item)
                        break@loop
                    }
                }
            }
            return
        }
    }
}