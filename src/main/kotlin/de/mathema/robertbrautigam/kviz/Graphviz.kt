package de.mathema.robertbrautigam.kviz

import guru.nidi.graphviz.attribute.Font
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Node
import java.io.File
import guru.nidi.graphviz.engine.Graphviz as GraphvizEngine

class Graphviz() {
    fun view(nodes: List<Node>) {
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
}