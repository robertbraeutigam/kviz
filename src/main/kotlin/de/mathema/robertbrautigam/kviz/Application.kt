package de.mathema.robertbrautigam.kviz

import java.util.*

class Application(val kubernetes: Kubernetes, val graphviz: Graphviz) {
    val currentObjects = CurrentObjects()

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
        val objects = kubernetes.objects()
        val now = Date()
        currentObjects.update(now, objects)
        graphviz.view(currentObjects.view(now))
    }
}
