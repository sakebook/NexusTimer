package com.sakebook.android.nexustimer.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast

/**
 * Created by sakemotoshinya on 16/06/06.
 */
class AlarmBroadcastReceiver: BroadcastReceiver() {

    val LAUNCHER = "com.google.android.leanbacklauncher"

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Received ", Toast.LENGTH_LONG).show()
        val pm = context.packageManager
        val targetIntent = Intent().apply { `package` = LAUNCHER }
        val resInfo = pm.queryIntentActivities(targetIntent, PackageManager.MATCH_DEFAULT_ONLY)
        val newIntent = resInfo
                .filter { it.activityInfo.packageName == LAUNCHER }
                .first().let {
                    Intent(Intent.ACTION_MAIN).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED//Intent.FLAG_ACTIVITY_NEW_TASK
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        component = ComponentName(LAUNCHER, it.activityInfo.name)
                    }
                }
        context.startActivity(newIntent)
    }
}