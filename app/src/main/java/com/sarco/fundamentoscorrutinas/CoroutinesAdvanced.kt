package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val countries = listOf("Santander", "CDMX", "Lima", "Buenos Aires", "Santiago")

fun main() {
    basicChannel()
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
