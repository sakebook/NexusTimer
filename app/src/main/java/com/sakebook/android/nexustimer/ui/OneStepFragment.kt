package com.sakebook.android.nexustimer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.sakebook.android.nexustimer.R

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class OneStepFragment: GuidedStepFragment() {

    private val POSITIVE = 0L
    private val NEGATIVE = 1L

    private val ARG_ID = "arg_id"

    fun newInstance(id: Long): OneStepFragment {
        return OneStepFragment().apply {
            val args = Bundle()
            args.putLong(ARG_ID, id)
            arguments = args
        }
    }


    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.dialog_title)
//        val breadcrumb = getString(R.string.guidedstep_first_breadcrumb)
        val description = getString(R.string.dialog_description)
        val icon = activity.getDrawable(R.mipmap.ic_launcher)
        return GuidanceStylist.Guidance(title, description, "", icon)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val parent = activity as SimpleDialogActivity
        parent.addAction(actions, POSITIVE,
                resources.getString(R.string.dialog_ok_title),
                resources.getString(R.string.dialog_ok_description))
        parent.addAction(actions, NEGATIVE,
                resources.getString(R.string.dialog_ng_title),
                resources.getString(R.string.dialog_ng_description))
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        if (action!!.id == POSITIVE) {
            val data = Intent();
            val bundle = Bundle();
            bundle.putLong(ARG_ID, arguments.getLong(ARG_ID));
            data.putExtras(bundle);
            activity.setResult(Activity.RESULT_OK, data)
            activity.finishAfterTransition()
        } else {
            activity.setResult(Activity.RESULT_CANCELED)
            activity.finishAfterTransition()
        }
    }
}