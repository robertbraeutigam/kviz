package de.mathema.robertbrautigam.kviz

import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.attribute.Shape
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.Factory.node
import guru.nidi.graphviz.model.Node
import java.util.*

class CurrentObjects(var objects: Map<String, Changed<KubernetesObject>> = emptyMap()) {
    fun update(now: Date, newKubernetesObjects: List<KubernetesObject>) {
        val newObjects: MutableMap<String, Changed<KubernetesObject>> = mutableMapOf()
        for (newObject in newKubernetesObjects) {
            val oldObject = objects[newObject.name()]
            if (oldObject == null) {
                newObjects[newObject.name()] =
                    Changed(newObject, now)
            } else {
                oldObject.update(now, newObject)
                newObjects[newObject.name()] = oldObject
            }
        }
        objects = newObjects
    }

    fun view(now: Date) = this.objects.values.map {
        view(it.obj, node(it.obj.name())
            .with(if (it.isUpdating(now)) {
                Style.FILLED.and(Style.BOLD)
            } else {
                Style.FILLED
            }))
    }

    private fun view(obj: KubernetesObject, node: Node) = when(obj) {
        is Pod -> node
            .with(Label.lines(obj.name, "("+obj.status+")"))
            .link(Factory.to(node(obj.controlledBy)))
        is ReplicaSet -> node
            .with(Shape.RECTANGLE, Color.BISQUE.fill())
    }
}
