package com.sakebook.android.nexustimer.model

/**
 * Created by sakemotoshinya on 16/06/08.
 */
enum class Week(val id: Long) {
    Sun(10),
    Mon(11),
    Tue(12),
    Wed(13),
    Thu(14),
    Fri(15),
    Sat(16),
    ;

    val on = "ON"
    val off = "OFF"
}