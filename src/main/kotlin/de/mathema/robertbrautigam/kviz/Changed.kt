package de.mathema.robertbrautigam.kviz

import java.util.*

private const val CHANGE_TIME = 4000L

class Changed<T>(var obj: T, var at: Date) {
    fun isUpdating(now: Date) = now.time < at.time + CHANGE_TIME

    fun update(now: Date, newObj: T) {
        if (newObj != obj) {
            obj = newObj
            at = now
        }
    }
}




