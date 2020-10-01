package de.mathema.robertbrautigam.kviz

import arrow.core.Either.Companion.left

fun <M, F> F.runApplication(currentObjects: CurrentObjects)
        where F: FileReader<M>, F: GraphizOutput<M>, F: Timed<M> =
    tailRecM(currentObjects, ::loopBody)

private fun <M, F> F.loopBody(currentObjects: CurrentObjects)
        where F: FileReader<M>, F: GraphizOutput<M>, F: Timed<M> = fx.monad {
    sleep(1000L).bind()
    val newObjects = runBody(currentObjects).bind()
    left(newObjects)
}

private fun <M, F> F.runBody(currentObjects: CurrentObjects)
        where F: FileReader<M>, F: GraphizOutput<M>, F: Timed<M> = fx.monad {
    val objects = objects().bind()
    val now = now().bind()
    val newObjects = currentObjects.update(now, objects)
    renderToFile(currentObjects, now).bind()
    newObjects
}
