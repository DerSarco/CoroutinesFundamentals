package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
val names = listOf("Carlos", "Alain", "Miguel", "Alberto", "Carla",
    "Natali", "Maria", "Mia", "Carmen")

fun main() {
    newTask()
}

fun newTask() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Un error a ocurrido, favor comunicarlo al administrador, " +
                "Info: $throwable in $coroutineContext")
    }
    runBlocking {
        val channel = Channel<String>(2)
            CoroutineScope(Job()).launch(exceptionHandler) {
                names.forEach{ name ->
                    channel.send(name)
                    if(name == "Carmen"){
                        println(produceDeveloper(name).receive())
                        channel.close()
                        this.cancel()
                    }
                }
            }
        channel.consumeEach {
            println(it)
        }
    }
}

fun CoroutineScope.produceDeveloper(name: String): ReceiveChannel<String> = produce {
    send("$name es una gran programadora!")
}



