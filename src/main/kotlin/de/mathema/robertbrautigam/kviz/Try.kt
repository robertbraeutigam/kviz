package de.mathema.robertbrautigam.kviz

sealed class Try<T> {
    companion object {
        operator fun <T> invoke(block: () -> T): Try<T> =
            try {
                Successful(block())
            } catch (e: Throwable) {
                Failure(e)
            }
    }
}

data class Successful<T>(val value: T) : Try<T>()

data class Failure<T>(val e: Throwable) : Try<T>()

fun <T, R> Try<T>.map(f: (T) -> R): Try<R> = when(this) {
    is Successful -> Successful(f(this.value))
    is Failure -> Failure(this.e)
}

fun <T, R> Try<T>.flatMap(f: (T) -> Try<R>): Try<R> = when(this) {
    is Successful -> f(this.value)
    is Failure -> Failure(this.e)
}
