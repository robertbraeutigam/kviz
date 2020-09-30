package de.mathema.robertbrautigam.kviz

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.syntax.collections.flatten
import java.io.File

fun objects() = IO.fx {
    val pods = objects("pods", ::parsePod).bind()
    val replicaSets = objects("replicasets", ::parseReplicaSet).bind()
    pods.plus(replicaSets).flatten()
}

private fun <T> objects(type: String, parse: (Map<String, String>) -> T) =
    descriptionBlocks(type).map { list ->
        list.map {
            parse(descriptionAttrs(it))
        }
    }

private fun descriptionBlocks(type: String) = descriptionText(type)
    .map { it.split(Regex("\n\n")) }

private fun descriptionText(type: String) = IO {
    File("kubernetes-$type")
        .readText()
}

/* This is for real kubectl
private fun descriptionText(type: String): List<String> {
    val proc = ProcessBuilder("kubectl", "describe", type)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
    proc.waitFor(1, TimeUnit.MINUTES)
    return proc.inputStream.bufferedReader()
        .readText()
}
*/

private fun descriptionAttrs(description: String) = description
    .lines()
    .map { Pair(it.substringBefore(":").trim(), it.substringAfter(":", "").trim()) }
    .toMap()

