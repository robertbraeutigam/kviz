package de.mathema.robertbrautigam.kviz

import java.util.*

private const val CHANGE_TIME = 4000L

data class Changed<T>(val obj: T, val at: Date)

fun Changed<*>.isUpdating(now: Date) = now.time < this.at.time + CHANGE_TIME

fun <T> Changed<T>.update(now: Date, newObj: T) = if (newObj != this.obj) {
    Changed(newObj, now)
} else {
    this
}
