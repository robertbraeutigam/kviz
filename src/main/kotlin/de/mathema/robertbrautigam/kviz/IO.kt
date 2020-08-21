package de.mathema.robertbrautigam.kviz

data class IO<T>(val block: () -> T){
    companion object {
        operator fun <T> invoke(block: () -> T) = IO(block)
    }
}

fun <T> IO<T>.runUnsafe() = this.block()

fun <T, R> IO<T>.map(f: (T) -> R): IO<R> = IO { f(runUnsafe()) }

fun <T, R> IO<T>.flatMap(f: (T) -> IO<R>): IO<R> = IO { f(runUnsafe()).runUnsafe() }
