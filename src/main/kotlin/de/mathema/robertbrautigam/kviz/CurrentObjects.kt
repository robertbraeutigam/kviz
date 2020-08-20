package de.mathema.robertbrautigam.kviz

import arrow.core.getOrElse
import arrow.core.toOption
import java.util.*

class CurrentObjects(private var objects: Map<String, Changed<KubernetesObject>> = emptyMap()) {
    fun update(now: Date, newKubernetesObjects: List<KubernetesObject>) {
        objects = newKubernetesObjects
              .map { newObject ->
                  objects[newObject.name()].toOption()
                      .map { it.update(now, newObject); it }
                      .getOrElse { Changed(newObject, now) }
              }
              .map { it.obj.name() to it }
              .toMap()
    }

    fun renderToFile(now: Date) {
        val graphviz = Graphviz()
        objects.values.forEach {
            if (it.isUpdating(now)) {
                graphviz.addChanged(it.obj)
            } else {
                graphviz.addUnchanged(it.obj)
            }
        }
        graphviz.renderToFile()
    }
}
