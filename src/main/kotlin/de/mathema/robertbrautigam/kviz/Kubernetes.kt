package de.mathema.robertbrautigam.kviz

import java.io.File

fun objects(): IO<List<KubernetesObject>> {
    val pods = objects("pods", ::parsePod)
    val replicaSets = objects("replicasets", ::parseReplicaSet)
    return pods.flatMap { ps ->
        replicaSets.map { rs ->
            ps.plus(rs).filterNotNull()
        }
    }
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

