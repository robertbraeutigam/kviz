package de.mathema.robertbrautigam.kviz

import arrow.core.getOrElse
import arrow.core.toOption
import java.util.*

data class CurrentObjects(val objects: Map<String, Changed<KubernetesObject>> = emptyMap())

fun CurrentObjects.update(now: Date, newKubernetesObjects: List<KubernetesObject>) =
    CurrentObjects(newKubernetesObjects
        .map { newObject ->
            this.objects[name(newObject)].toOption()
                .map { it.update(now, newObject) }
                .getOrElse { Changed(newObject, now) }
        }
        .map { name(it.obj) to it }
        .toMap())

fun CurrentObjects.renderToFile(now: Date) =
    this.objects.values.fold(Graphviz()) { graph: Graphviz, changed: Changed<KubernetesObject> ->
        if (changed.isUpdating(now)) {
            graph.addChanged(changed.obj)
        } else {
            graph.addUnchanged(changed.obj)
        }
    }.renderToFile()
