package com.sakebook.android.nexustimer.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepFragment
import android.support.v17.leanback.widget.*
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.sakebook.android.nexustimer.R
import com.sakebook.android.nexustimer.model.Week
import java.util.*

class ScheduleCreateStepActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null == savedInstanceState) {
            GuidedStepFragment.addAsRoot(this, WeekFragment(), android.R.id.content)
        }
    }

    companion object {
        private val CONTINUE = 0
        private val BACK = 1

        @JvmStatic
        fun addAction(context: Context, actions: MutableList<GuidedAction>, id: Long, title: String, desc: String) {
            actions.add(GuidedAction.Builder(context).id(id).title(title).description(desc).build())
        }
    }

    class WeekFragment : GuidedStepFragment() {

        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = getString(R.string.schedule_title)
            val description = getString(R.string.schedule_description)
            val icon = activity.getDrawable(R.mipmap.ic_launcher)
            return GuidanceStylist.Guidance(title, description, "", icon)
        }

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "WeekFragment#onCreateActions")
            Week.values().forEachIndexed { i, week ->
                val weekAction = GuidedAction.Builder(activity)
                        .id(week.id)
                        .title(week.name)
                        .description(week.off)
                        .build()
                val onAction = GuidedAction.Builder(activity).title("ON").description("有効にします").checkSetId(GuidedAction.DEFAULT_CHECK_SET_ID).id(i.toLong()).build()
                val offAction = GuidedAction.Builder(activity).title("OFF").description("無効にします").checkSetId(GuidedAction.DEFAULT_CHECK_SET_ID).id(i.toLong()).build()
                weekAction.subActions = listOf(onAction, offAction)
                actions.add(weekAction)
            }
        }

        override fun onCreateButtonActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "WeekFragment#onCreateButtonActions")
            val nextAction = GuidedAction.Builder(activity).title("next").description("nextだよ").id(0).build()
            val cancelAction = GuidedAction.Builder(activity).title("cancel").description("cancelだよ").id(1).build()
            actions.add(nextAction)
            actions.add(cancelAction)
        }


        override fun onGuidedActionClicked(action: GuidedAction) {
            Log.d("TAG", "WeekFragment#onGuidedActionClicked: ${action.id}, ${action.title}")
            when(action.id.toInt()) {
                CONTINUE -> {
                    val allows = IntArray(7)
                    this.actions.forEachIndexed { i, action ->
                        allows[i] = if (action.description == "ON") 1 else 0
                    }
                    GuidedStepFragment.add(fragmentManager, TimeFragment().newInstance(allows))
                }
                BACK -> activity.finishAfterTransition()
                else -> expandSubActions(action)
            }
        }

        override fun onSubGuidedActionClicked(action: GuidedAction): Boolean {
            Log.d("TAG", "WeekFragment#onSubGuidedActionClicked: ${action.description},  ${action.id}, ${action.checkSetId}")
            this.actions[action.id.toInt()].description = action.title
            this.notifyActionChanged(action.id.toInt())
            return super.onSubGuidedActionClicked(action)
        }
    }


    class TimeFragment : GuidedStepFragment() {

        private val ARG_WEEKS = "arg_weeks"

        fun newInstance(weekAllows: IntArray): TimeFragment {
            return TimeFragment().apply {
                val args = Bundle()
                args.putIntArray(ARG_WEEKS, weekAllows)
                arguments = args
            }
        }


        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = getString(R.string.schedule_title)
            val weeks = arguments.getIntArray(ARG_WEEKS)
            weeks.forEach { Log.d("TAG", "TimeFragment#${it}") }
            val description = "time"
            val icon = activity.getDrawable(R.mipmap.ic_launcher)
            return GuidanceStylist.Guidance(title, description, "", icon)
        }

        override fun onGuidedActionClicked(action: GuidedAction) {
            val actionPosition = this.actions.indexOf(action)
            val actionPosition1 = action.subActions.indexOf(action)
            Log.d("TAG", "WeekFragment#onGuidedActionClicked: ${action.id}, ${action.title}, index: ${actionPosition}, index: ${actionPosition1}")

            if (action.id == CONTINUE.toLong()) {
                val fm = fragmentManager
//                GuidedStepFragment.add(fm, WeekFragment())
                activity.finishAfterTransition()
//                finishGuidedStepFragments()
            } else {
                expandSubActions(action)
//                fragmentManager.popBackStack()
//                activity.finishAfterTransition()
            }
        }

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "WeekFragment#onCreateActions")

            val onAction = GuidedAction.Builder(activity).title("ON").description("1").build()
            val offAction = GuidedAction.Builder(activity).title("OFF").description("23").build()

            Week.values().forEach {
                val weekAction = GuidedAction.Builder(activity)
                        .id(it.id)
                        .title(it.name)
                        .description("おふー")
                        .build()
                weekAction.subActions = listOf(onAction, offAction)
                actions.add(weekAction)
            }
        }

        override fun onCreateButtonActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "WeekFragment#onCreateButtonActions")
            super.onCreateButtonActions(actions, savedInstanceState)

            val hourAction = GuidedAction.Builder(activity).title("時間").description("時間だよ").id(24).build()
            val hourDetailList = mutableListOf<GuidedAction>()
            for (i in 0..23) {
                hourDetailList.add(GuidedAction.Builder(activity)
                        .id(i.toLong())
                        .title("$i")
                        .description("$i")
                        .build())
            }
            hourAction.subActions = hourDetailList

            val minuteAction = GuidedAction.Builder(activity).title("分").description("分だよ").id(48).build()
            val minuteDetailList = mutableListOf<GuidedAction>()
            for (i in 0..59) {
                minuteDetailList.add(GuidedAction.Builder(activity)
                        .id(i.toLong())
                        .title("$i")
                        .description("$i")
                        .build())
            }
            minuteAction.subActions = minuteDetailList

            actions.add(hourAction)
            actions.add(minuteAction)
        }

        override fun onSubGuidedActionClicked(action: GuidedAction): Boolean {
            Log.d("TAG", "WeekFragment#onSubGuidedActionClicked: ${action.let { it.description }} ${action.id}")
            when(action.id) {
                Week.Mon.id -> Log.d("TAG", "WeekFragment#onSubGuidedActionClicked: ${action.let { it.description }}")
            }
            this.actions.get(action.id.toInt()).description = "hoge"
            return super.onSubGuidedActionClicked(action)
        }

        override fun onGuidedActionEditedAndProceed(action: GuidedAction?): Long {
            Log.d("TAG", "WeekFragment#onGuidedActionEditedAndProceed: ${action?.let { it.title }}")
            return super.onGuidedActionEditedAndProceed(action)
        }
    }


}
