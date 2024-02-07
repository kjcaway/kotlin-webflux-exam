package me.exam.ktwebfx

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class KtwebfxApplication

fun main(args: Array<String>) {
    BlockHound.install()
    runApplication<KtwebfxApplication>(*args)
}
