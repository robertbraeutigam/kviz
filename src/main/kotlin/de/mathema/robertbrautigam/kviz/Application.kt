package de.mathema.robertbrautigam.kviz

import java.util.*

tailrec fun runApplication(currentObjects: CurrentObjects) {
    runApplication(loopBody(currentObjects))
}

private fun loopBody(currentObjects: CurrentObjects): CurrentObjects {
    Thread.sleep(1000L)
    return runBody(currentObjects)
}

private fun runBody(currentObjects: CurrentObjects): CurrentObjects {
    val objects = objects()
    val now = Date()
    val newObjects = update(currentObjects, now, objects)
    renderToFile(currentObjects, now)
    return newObjects
}
