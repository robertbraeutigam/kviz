package de.mathema.robertbrautigam.kviz

import arrow.core.getOrElse
import arrow.core.toOption
import java.util.*

class CurrentObjects(val objects: Map<String, Changed<KubernetesObject>> = emptyMap())

fun update(oldObjects: CurrentObjects, now: Date, newKubernetesObjects: List<KubernetesObject>) =
    CurrentObjects(newKubernetesObjects
        .map { newObject ->
            oldObjects.objects[name(newObject)].toOption()
                .map { update(it, now, newObject) }
                .getOrElse { Changed(newObject, now) }
        }
        .map { name(it.obj) to it }
        .toMap())

fun renderToFile(objects: CurrentObjects, now: Date) {
    renderToFile(objects.objects.values.fold(Graphviz()) { graph: Graphviz, changed: Changed<KubernetesObject> ->
        if (isUpdating(changed, now)) {
            addChanged(graph, changed.obj)
        } else {
            addUnchanged(graph, changed.obj)
        }
    })
}
