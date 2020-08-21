package de.mathema.robertbrautigam.kviz

sealed class KubernetesObject

data class ReplicaSet(val name: String) : KubernetesObject()

fun parseReplicaSet(attrs: Map<String, String>): ReplicaSet? =
    attrs["Name"]?.run(::ReplicaSet)

data class Pod(val name: String, val status: String, val controlledBy: String) : KubernetesObject()

fun parsePod(attrs: Map<String, String>): Pod? {
    val name = attrs["Name"]
    val status = attrs["Status"]
    val controlledBy = attrs["Controlled By"]
    if (name == null || status == null || controlledBy == null) {
        return null
    }
    return Pod(name, status, controlledBy)
}

fun name(obj: KubernetesObject) = when (obj) {
    is ReplicaSet -> "ReplicaSet/${obj.name}"
    is Pod -> "Pod/${obj.name}"
}