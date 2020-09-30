package de.mathema.robertbrautigam.kviz

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOption

sealed class KubernetesObject

data class ReplicaSet(val name: String) : KubernetesObject()

fun parseReplicaSet(attrs: Map<String, String>) = Option.fx {
    val name = attrs.getOption("Name").bind()
    ReplicaSet(name)
}

data class Pod(val name: String, val status: String, val controlledBy: String) : KubernetesObject()

fun parsePod(attrs: Map<String, String>) = Option.fx {
    val name = attrs.getOption("Name").bind()
    val status = attrs.getOption("Status").bind()
    val controlledBy = attrs.getOption("Controlled By").bind()
    Pod(name, status, controlledBy)
}

fun name(obj: KubernetesObject) = when (obj) {
    is ReplicaSet -> "ReplicaSet/${obj.name}"
    is Pod -> "Pod/${obj.name}"
}