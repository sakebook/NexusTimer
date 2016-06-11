package com.sakebook.android.nexustimer.ui

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepFragment
import android.support.v17.leanback.widget.*
import android.text.InputType
import android.util.Log
import android.widget.Toast
import com.sakebook.android.nexustimer.R
import com.sakebook.android.nexustimer.model.Week

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
        private val TIME_PICKER = 10

        @JvmStatic
        fun addAction(context: Context, actions: MutableList<GuidedAction>, id: Long, title: String, desc: String) {
            actions.add(GuidedAction.Builder(context).id(id).title(title).description(desc).build())
        }
    }

    class WeekFragment : GuidedStepFragment() {

        val weeks = Week.values()

        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = getString(R.string.schedule_title)
            val description = getString(R.string.schedule_description)
            val icon = activity.getDrawable(R.mipmap.ic_launcher)
            return GuidanceStylist.Guidance(title, description, "Step: 1/2", icon)
        }

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "WeekFragment#onCreateActions")
            weeks.forEachIndexed { i, week ->
                val weekAction = GuidedAction.Builder(activity)
                        .id(week.id)
                        .title(week.name)
                        .description(week.label())
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
                    when(weeks.any { it.enable }) {
                        true -> GuidedStepFragment.add(fragmentManager, TimeFragment().newInstance(weeks))
                        false -> Toast.makeText(activity, "最低どれかはONにしないと。。", Toast.LENGTH_SHORT).show()
                    }
                }
                BACK -> activity.finishAfterTransition()
                else -> expandSubActions(action)
            }
        }

        override fun onSubGuidedActionClicked(action: GuidedAction): Boolean {
            Log.d("TAG", "WeekFragment#onSubGuidedActionClicked: ${action.title}, ${action.description},  ${action.id}, ${action.checkSetId}")
            val index = action.id.toInt()
            if (action.title == "ON") {
                weeks[index].enable = true
            } else {
                weeks[index].enable = false
            }
            this.actions[index].description = action.title
            this.notifyActionChanged(index)
            return super.onSubGuidedActionClicked(action)
        }
    }


    class TimeFragment : GuidedStepFragment() {

        private val ARG_WEEKS = "arg_weeks"
        private var hour = 0
        private var minute = 0

        fun newInstance(weeks: Array<Week>): TimeFragment {
            return TimeFragment().apply {
                val args = Bundle()
                args.putSerializable(ARG_WEEKS, weeks)
                arguments = args
            }
        }

        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = getString(R.string.schedule_title)
            val weeks = arguments.getSerializable(ARG_WEEKS) as Array<Week>
            val description = weeks.filter { it.enable }
                                    .map { it.name }
                                    .reduce { s1, s2 -> "$s1, $s2" }
            val icon = activity.getDrawable(R.mipmap.ic_launcher)
            return GuidanceStylist.Guidance(title, description, "Step: 2/2", icon)
        }

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "TimeFragment#onCreateActions")
            val action = GuidedAction.Builder(activity)
                    .title("時間")
                    .description("00:00")
                    .id(10)
                    .editInputType(InputType.TYPE_CLASS_NUMBER and InputType.TYPE_MASK_CLASS)
                    .build()
            actions.add(action)
        }

        override fun onCreateButtonActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            Log.d("TAG", "TimeFragment#onCreateButtonActions")
            val nextAction = GuidedAction.Builder(activity).title("create").description("createだよ").id(0).build()
            val cancelAction = GuidedAction.Builder(activity).title("back").description("backだよ").id(1).build()
            actions.add(nextAction)
            actions.add(cancelAction)
        }


        override fun onGuidedActionClicked(action: GuidedAction) {
            val actionPosition = this.actions.indexOf(action)
            Log.d("TAG", "TimeFragment#onGuidedActionClicked: ${action.id}, ${action.title}, index: ${actionPosition}")
            when(action.id.toInt()) {
                CONTINUE -> {
                    val data = Intent();
                    val bundle = Bundle();
                    bundle.putSerializable(ARG_WEEKS, arguments.getSerializable(ARG_WEEKS))
                    bundle.putInt("hour", hour)
                    bundle.putInt("minute", minute)
                    data.putExtras(bundle);
                    activity.setResult(Activity.RESULT_OK, data)
                    activity.finishAfterTransition()
                }
                TIME_PICKER -> {
                    showTimeDialog(TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        val h = if (hour < 10) "0$hour" else hour.toString()
                        val m = if (minute < 10) "0$minute" else minute.toString()
                        this.actions[0].description = "$h:$m"
                        this.hour = hour
                        this.minute = minute
                        this.notifyActionChanged(0)
                    })
                }
                else -> fragmentManager.popBackStack()
            }
        }

        private fun showTimeDialog(listener: TimePickerDialog.OnTimeSetListener) {
            val timeDialog = TimePickerDialog(activity,
                    android.R.style.Theme_Material_Dialog_Alert,
                    listener,
                    0,
                    0,
                    true);
            timeDialog.setCancelable(false)
            timeDialog.show()
        }
    }
}
