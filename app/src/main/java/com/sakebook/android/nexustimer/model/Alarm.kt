package com.sakebook.android.nexustimer.model

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

/**
 * Created by sakemotoshinya on 16/06/11.
 */
@Table
class Alarm {

    @PrimaryKey(autoincrement = true) val id: Long
    @Column val hour: Int
    @Column val minute: Int
    @Column val sun: Boolean
    @Column val mon: Boolean
    @Column val tue: Boolean
    @Column val wed: Boolean
    @Column val thu: Boolean
    @Column val fri: Boolean
    @Column val sat: Boolean

    constructor(@Setter("id") id: Long,
                @Setter("sun") sun: Boolean,
                @Setter("mon") mon: Boolean,
                @Setter("tue") tue: Boolean,
                @Setter("wed") wed: Boolean,
                @Setter("thu") thu: Boolean,
                @Setter("fri") fri: Boolean,
                @Setter("sat") sat: Boolean,
                @Setter("hour") hour: Int,
                @Setter("minute") minute: Int) {
        this.id = id
        this.hour = hour
        this.minute = minute
        this.sun = sun
        this.mon = mon
        this.tue = tue
        this.wed = wed
        this.thu = thu
        this.fri = fri
        this.sat = sat
    }
}