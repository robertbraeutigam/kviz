package de.mathema.robertbrautigam.kviz

import arrow.Kind
import arrow.extension
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.IOMonad
import arrow.fx.fix
import arrow.fx.typeclasses.Duration
import arrow.typeclasses.Monad
import de.mathema.robertbrautigam.kviz.io.application.application
import guru.nidi.graphviz.engine.Renderer
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

fun main() {
    IO.application().runApplication(CurrentObjects()).fix().unsafeRunSync()
}

interface Timed<M> {
    fun now(): Kind<M, Date>

    fun sleep(millis: Long): Kind<M, Unit>
}

interface FileReader<M> {
    fun readFile(file: String): Kind<M, String>
}

interface GraphizOutput<M> {
    fun Renderer.toOutput(file: File): Kind<M, Unit>
}

interface Application<M>: Timed<M>, FileReader<M>, GraphizOutput<M>, Monad<M>

@extension
interface IOApplication: Application<ForIO>, IOMonad {
    override fun now() = IO {
        Date()
    }

    override fun sleep(millis: Long) =
        arrow.fx.extensions.io.concurrent.sleep(Duration(millis, TimeUnit.MILLISECONDS))

    override fun readFile(file: String) = IO {
        File(file).readText()
    }

    override fun Renderer.toOutput(file: File) = IO {
        this.toFile(file)
        Unit
    }
}
