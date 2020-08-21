package de.mathema.robertbrautigam.kviz

import java.io.File

fun objects(): List<KubernetesObject> {
    val pods = objects("pods", ::parsePod)
    val replicaSets = objects("replicasets", ::parseReplicaSet)
    return pods.union(replicaSets).toList().filterNotNull()
}

private fun <T> objects(type: String, parse: (Map<String, String>) -> T): List<T> {
    val description = descriptionBlocks(type)
    return description.map {
        parse(descriptionAttrs(it))
    }
}

private fun descriptionBlocks(type: String) = descriptionText(type)
    .split(Regex("\n\n"))

private fun descriptionText(type: String): String {
    return File("kubernetes-$type")
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

