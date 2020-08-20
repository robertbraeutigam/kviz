package de.mathema.robertbrautigam.kviz

import java.util.*

class Application {
    private val currentObjects = CurrentObjects()

    fun runApplication() {
        while (true) {
            loopBody()
        }
    }

    private fun loopBody() {
        runBody()
        Thread.sleep(1000L)
    }

    private fun runBody() {
        val objects = Kubernetes().objects()
        val now = Date()
        currentObjects.update(now, objects)
        currentObjects.renderToFile(now)
    }
}
