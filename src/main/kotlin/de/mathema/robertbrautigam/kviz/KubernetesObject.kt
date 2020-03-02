package de.mathema.robertbrautigam.kviz

sealed class KubernetesObject() {
    abstract fun name(): String
}

data class ReplicaSet(val name: String): KubernetesObject() {
    override fun name() = "ReplicaSet/$name"

    companion object {
        fun parseReplicaSet(attrs: Map<String, String>): ReplicaSet? =
            attrs["Name"]?.run(::ReplicaSet)
    }
}

data class Pod(val name: String, val status: String, val controlledBy: String): KubernetesObject() {
    override fun name() = "Pod/$name"

    companion object {
        fun parsePod(attrs: Map<String, String>): Pod? {
            val name = attrs["Name"]
            val status = attrs["Status"]
            val controlledBy = attrs["Controlled By"]
            if (name == null || status == null || controlledBy == null) {
                return null
            }
            return Pod(name, status, controlledBy)
        }
    }
}

