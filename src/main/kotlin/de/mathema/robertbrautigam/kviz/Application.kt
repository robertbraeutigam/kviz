package de.mathema.robertbrautigam.kviz

import arrow.core.Either.Companion.left
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.typeclasses.Duration
import java.util.*
import java.util.concurrent.TimeUnit

fun runApplication(currentObjects: CurrentObjects) =
    IO.tailRecM(currentObjects, ::loopBody)

private fun loopBody(currentObjects: CurrentObjects) = IO.fx {
    sleep(Duration(1000L, TimeUnit.MILLISECONDS)).bind()
    val newObjects = runBody(currentObjects).bind()
    left(newObjects)
}

private fun runBody(currentObjects: CurrentObjects) =
    objects().flatMap { objects ->
        IO { Date() }.flatMap { now ->
            val newObjects = currentObjects.update(now, objects)
            currentObjects.renderToFile(now).map {
                newObjects
            }
        }
    }
