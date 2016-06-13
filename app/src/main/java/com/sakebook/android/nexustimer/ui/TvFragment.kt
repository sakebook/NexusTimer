package com.sakebook.android.nexustimer.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.sakebook.android.nexustimer.R
import com.sakebook.android.nexustimer.model.*
import com.sakebook.android.nexustimer.presenter.GridItemPresenter
import com.sakebook.android.nexustimer.presenter.ItemViewClickedListener
import com.sakebook.android.nexustimer.presenter.OffTimerItemPresenter
import java.util.*

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class TvFragment: BrowseFragment() {

    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    val DELETE_REQUEST_CODE = 100
    val CREATE_REQUEST_CODE = 101
    private val ARG_HOUR = "arg_hour"
    private val ARG_MINUTE = "arg_minute"
    private val ARG_WEEKS = "arg_weeks"
    lateinit var orma: OrmaDatabase

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        loadRows()
        setupEventListeners()
    }

    private fun setupUIElements() {
        title = resources.getString(R.string.app_name)
        brandColor = ContextCompat.getColor(activity, R.color.primary)
        searchAffordanceColor = ContextCompat.getColor(activity, R.color.accent)
    }

    private fun loadRows() {
        rowsAdapter.add(createDefaultTimer())
        orma = OrmaDatabase.builder(activity).build();
        val alarms = orma.relationOfAlarm().toList()
        alarms.forEach {
            Log.d("TAG", "loadRows: id: ${it.id}, time: ${it.hour}:${it.minute}")
        }

        // TODO: load from DB
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
        OffTimer.values().forEach { gridRowAdapter.add(it) }
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
            openCreateAlarm()

        }
        onItemViewClickedListener = ItemViewClickedListener(this)
//        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private fun openCreateAlarm() {
        val intent = Intent(activity, ScheduleCreateStepActivity::class.java)
        val bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
        startActivityForResult(intent, CREATE_REQUEST_CODE, bundle)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(activity, "onActivityResult: ${requestCode}, ${resultCode}", Toast.LENGTH_LONG).show()
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        data?.let {
            when(requestCode) {
                CREATE_REQUEST_CODE -> create(it)
                DELETE_REQUEST_CODE -> delete(it)
                else -> return
            }
        }
    }

    private fun create(data: Intent) {
        Toast.makeText(activity, "onActivityResult create: ${data.getSerializableExtra(ARG_WEEKS)}", Toast.LENGTH_LONG).show()
        val hour = data.getIntExtra(ARG_HOUR, -1)
        val minute = data.getIntExtra(ARG_MINUTE, -1)
        val weeks = data.getSerializableExtra(ARG_WEEKS) as Array<Week>?
        if (hour < 0 || minute < 0 || weeks == null) {
            Toast.makeText(activity, "onActivityResult create: fail!!", Toast.LENGTH_LONG).show()
            return
        }
        weeks.forEach {
            Log.d("TAG", "create: id: ${it.id}, name: ${it.name}, status: ${it.label()}")
        }
        orma = OrmaDatabase.builder(activity).build();
        // FIXME: User constructor Array<Week>
        val alarm = Alarm(0,
                weeks[0].enable,
                weeks[1].enable,
                weeks[2].enable,
                weeks[3].enable,
                weeks[4].enable,
                weeks[5].enable,
                weeks[6].enable,
                hour, minute)
        orma.insertIntoAlarm(alarm)
    }

    private fun delete(data: Intent) {
        val id = data.extras.getLong(SimpleDialogActivity.ARG_ID)
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
    }
}