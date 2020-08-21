package de.mathema.robertbrautigam.kviz

import java.util.*

fun runApplication(currentObjects: CurrentObjects): IO<Unit> =
    loopBody(currentObjects).flatMap(::runApplication)

private fun loopBody(currentObjects: CurrentObjects): IO<CurrentObjects> {
    Thread.sleep(1000L)
    return runBody(currentObjects)
}

private fun runBody(currentObjects: CurrentObjects) =
    objects().map { objects ->
        val now = Date()
        val newObjects = currentObjects.update(now, objects)
        currentObjects.renderToFile(now)
        newObjects
    }
