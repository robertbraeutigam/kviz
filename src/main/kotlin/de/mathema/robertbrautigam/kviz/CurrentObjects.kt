package de.mathema.robertbrautigam.kviz

import arrow.core.getOrElse
import arrow.core.toOption
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
        objects = newKubernetesObjects
              .map { newObject ->
                  objects[newObject.name()].toOption()
                      .map { it.update(now, newObject); it }
                      .getOrElse { Changed(newObject, now) }
              }
              .map { it.obj.name() to it }
              .toMap()
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
