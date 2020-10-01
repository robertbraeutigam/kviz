package de.mathema.robertbrautigam.kviz

import arrow.core.Either.Companion.left
import arrow.typeclasses.Monad

fun <M> Application<M>.runApplication(currentObjects: CurrentObjects) =
    tailRecM(currentObjects, ::loopBody)

private fun <M> Application<M>.loopBody(currentObjects: CurrentObjects) = fx.monad {
    sleep(1000L).bind()
    val newObjects = runBody(currentObjects).bind()
    left(newObjects)
}

private fun <M> Application<M>.runBody(currentObjects: CurrentObjects)= fx.monad {
    val objects = objects().bind()
    val now = now().bind()
    val newObjects = currentObjects.update(now, objects)
    renderToFile(currentObjects, now).bind()
    newObjects
}
