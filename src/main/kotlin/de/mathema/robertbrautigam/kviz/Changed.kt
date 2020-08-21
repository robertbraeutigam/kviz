package de.mathema.robertbrautigam.kviz

import java.util.*

private const val CHANGE_TIME = 4000L

class Changed<T>(val obj: T, val at: Date)

fun isUpdating(changed: Changed<*>, now: Date) = now.time < changed.at.time + CHANGE_TIME

fun <T> update(changed: Changed<T>, now: Date, newObj: T) = if (newObj != changed.obj) {
    Changed(newObj, now)
} else {
    changed
}
