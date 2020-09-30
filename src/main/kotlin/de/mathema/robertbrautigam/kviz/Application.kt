package de.mathema.robertbrautigam.kviz

import java.util.*

fun runApplication(currentObjects: CurrentObjects): IO<Unit> =
    loopBody(currentObjects).flatMap(::runApplication)

private fun loopBody(currentObjects: CurrentObjects) =
    IO { Thread.sleep(1000L) }.flatMap {
        runBody(currentObjects)
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
