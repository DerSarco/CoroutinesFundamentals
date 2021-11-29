package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val countries = listOf("Santander", "CDMX", "Lima", "Buenos Aires", "Santiago")

fun main() {
//    basicChannel()
    closeChannel()


}

fun closeChannel() {
    runBlocking {
        newTopic("Cerrar un canal")

        val channel = Channel<String>()

        launch {
            countries.forEach{
                channel.send(it)
                if(it == "Lima"){
                    channel.close()
                    return@launch
                }
            }
//            channel.close()
        }

//        for (value in channel){
//            println(value)
//        }\

        while (!channel.isClosedForReceive){
            println(channel.receive())
        }

//        channel.consumeEach {
//            println(it)
//        }

    }
}


fun basicChannel() {
    runBlocking {
        newTopic("Canal Básico")

        val channel = Channel<String>()
        launch {
            countries.forEach{
                channel.send(it)
            }
        }
/*
        repeat(5){
            println(channel.receive())
        }*/

        for (value in channel){
            println(value)
        }

    }

}
