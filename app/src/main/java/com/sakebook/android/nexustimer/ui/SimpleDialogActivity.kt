package com.sakebook.android.nexustimer.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepFragment
import android.support.v17.leanback.widget.GuidedAction

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class SimpleDialogActivity: Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null == savedInstanceState) {
            GuidedStepFragment.addAsRoot(this, OneStepFragment().newInstance(intent.getLongExtra(ARG_ID, -1L)), android.R.id.content)
        }
    }

    fun addAction(actions: MutableList<GuidedAction>, id: Long, title: String, desc: String) {
        actions.add(GuidedAction.Builder(this).id(id).title(title).description(desc).build())
    }

    companion object {
        val ARG_ID = "arg_id"

        @JvmStatic
        fun createIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, SimpleDialogActivity::class.java)
            intent.putExtra(ARG_ID, id)
            return intent
        }
    }


}