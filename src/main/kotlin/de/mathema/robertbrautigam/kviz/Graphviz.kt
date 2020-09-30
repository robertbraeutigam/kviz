package de.mathema.robertbrautigam.kviz

import arrow.fx.IO
import guru.nidi.graphviz.attribute.*
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Node
import java.io.File
import guru.nidi.graphviz.engine.Graphviz as GraphvizEngine

data class Graphviz(val nodes: List<Node> = emptyList())

fun Graphviz.renderToFile(): IO<Unit> {
    val graph = graph("kubernetes-graph").directed()
        .graphAttr()
        .with(Rank.dir(Rank.RankDir.RIGHT_TO_LEFT))
        .with(this.nodes)
        .nodeAttr().with(Font.size(24))
    return IO {
        GraphvizEngine
            .fromGraph(graph)
            .fontAdjust(0.85)
            .render(Format.PNG).toFile(File("kubernetes-graph.png"))
    }.map { Unit }
}

fun Graphviz.addUnchanged(obj: KubernetesObject) =
    Graphviz(this.nodes + listOf(decoratedNode(obj).with(Style.FILLED)))

fun Graphviz.addChanged(obj: KubernetesObject) =
    Graphviz(this.nodes + listOf(decoratedNode(obj).with(Style.FILLED.and(Style.BOLD))))

private fun decoratedNode(obj: KubernetesObject) = when (obj) {
    is Pod -> node(obj)
        .with(Label.lines(obj.name, "(" + obj.status + ")"))
        .link(Factory.to(Factory.node(obj.controlledBy)))
    is ReplicaSet -> node(obj)
        .with(Shape.RECTANGLE, Color.BISQUE.fill())
}

private fun node(obj: KubernetesObject) = Factory.node(name(obj))
