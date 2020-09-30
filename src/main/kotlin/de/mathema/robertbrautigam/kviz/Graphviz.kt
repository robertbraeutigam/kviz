package de.mathema.robertbrautigam.kviz

import guru.nidi.graphviz.attribute.*
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Node
import java.io.File
import guru.nidi.graphviz.engine.Graphviz as GraphvizEngine

class Graphviz {
    private var nodes = mutableListOf<Node>()

    fun renderToFile() {
        val graph = graph("kubernetes-graph").directed()
            .graphAttr()
            .with(Rank.dir(Rank.RankDir.RIGHT_TO_LEFT))
            .with(nodes)
            .nodeAttr().with(Font.size(24))
        GraphvizEngine
            .fromGraph(graph)
            .fontAdjust(0.85)
            .render(Format.PNG).toFile(File("kubernetes-graph.png"))
    }

    fun addUnchanged(obj: KubernetesObject) {
        nodes.add(renderToFile(obj).with(Style.FILLED))
    }

    fun addChanged(obj: KubernetesObject) {
        nodes.add(renderToFile(obj).with(Style.FILLED.and(Style.BOLD)))
    }

    private fun renderToFile(obj: KubernetesObject) = when(obj) {
        is Pod -> node(obj)
            .with(Label.lines(obj.name, "("+obj.status+")"))
            .link(Factory.to(Factory.node(obj.controlledBy)))
        is ReplicaSet -> node(obj)
            .with(Shape.RECTANGLE, Color.BISQUE.fill())
    }

    private fun node(obj: KubernetesObject) = Factory.node(obj.name())
}