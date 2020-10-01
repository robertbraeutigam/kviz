package de.mathema.robertbrautigam.kviz

import arrow.syntax.collections.flatten
import arrow.typeclasses.Monad

fun <M, F> F.objects()
        where F: FileReader<M>, F: Monad<M> = fx.monad {
    val pods = objects("pods", ::parsePod).bind()
    val replicaSets = objects("replicasets", ::parseReplicaSet).bind()
    pods.plus(replicaSets).flatten()
}

private fun <M, F, T> F.objects(type: String, parse: (Map<String, String>) -> T)
        where F: FileReader<M>, F: Monad<M> =
    descriptionBlocks(type).map { list ->
        list.map {
            parse(descriptionAttrs(it))
        }
    }

private fun <M, F> F.descriptionBlocks(type: String)
        where F: FileReader<M>, F: Monad<M> =
    descriptionText(type)
        .map { it.split(Regex("\n\n")) }

private fun <M> FileReader<M>.descriptionText(type: String) =
    readFile("kubernetes-$type")

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

