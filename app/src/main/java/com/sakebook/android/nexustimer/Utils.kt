package com.sakebook.android.nexustimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sakebook.android.nexustimer.receiver.AlarmBroadcastReceiver

/**
 * Created by sakemotoshinya on 16/06/06.
 */
object Utils {

    fun setAlarm(context: Context, minute: Int) {
        val time = minute * 1000 * 60
        val intent = Intent(context.applicationContext, AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pending);
    }
}