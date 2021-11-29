package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import java.util.concurrent.TimeoutException
import kotlin.ArithmeticException
import kotlin.Exception

val countries = listOf("Santander", "CDMX", "Lima", "Buenos Aires", "Santiago")

fun main() {
//    basicChannel()
//    closeChannel()
//    produceChannel()
//    pipeline()
//    bufferChannel()
    exceptions()
    readLine()

}

fun exceptions() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Notifica al programador... $throwable in $coroutineContext" )

        if(throwable is ArithmeticException) println("Mostrar mensaje de reintentar")
        println()
    }
    runBlocking {
        newTopic("Manejo de excepciones")
        launch {
            try {
                delay(100)
//                throw Exception()
            }catch (e: Exception){
                e.printStackTrace()
            }

        }

        val globalScope = CoroutineScope(Job() + exceptionHandler)
        globalScope.launch {
            delay(200)
            throw TimeoutException()
        }

        CoroutineScope(Job()).launch(exceptionHandler) {
            val result = async {
                delay(500)
                multiLambda(2, 3){
                    if(it > 5){
                        throw ArithmeticException()
                    }
                }
            }

            println("Result ${result.await()}")
        }


        val channel = Channel<String>()
        CoroutineScope(Job()).launch(exceptionHandler) {
            delay(800)
            countries.forEach{
                channel.send(it)
                if(it == "Lima") channel.close()
            }
        }

        channel.consumeEach {
            println(it)
        }

    }
}

fun bufferChannel() {
    runBlocking {
        newTopic("Buffer Para channels")
        val time = System.currentTimeMillis()
        val channel = Channel<String>()
        launch {
            countries.forEach{
                delay(100)
                channel.send(it)
            }

            channel.close()
        }

        launch {
            delay(1_000)
            channel.consumeEach {
                println(it)
            }
            println("Time: ${System.currentTimeMillis() - time}ms")
        }
        val bufferTime = System.currentTimeMillis()
        val bufferChannel = Channel<String>(2)
        launch {
            countries.forEach{
                delay(100)
                bufferChannel.send(it)
            }
            bufferChannel.close()
        }

        launch {
            delay(1_000)
            bufferChannel.consumeEach {
                println(it)
            }
            println("Buffer Time: ${System.currentTimeMillis() - bufferTime}ms")
        }
    }
}

fun pipeline() {
    runBlocking {
        newTopic("Pipelines")
        val citiesChannel = produceCities()
        val foodsChannel = produceFoods(citiesChannel)
        foodsChannel.consumeEach {
            println(it)
        }
        citiesChannel.cancel()
        foodsChannel.cancel()
        println("Todo esta 10/10")

    }
}

fun CoroutineScope.produceFoods(cities: ReceiveChannel<String>): ReceiveChannel<String> = produce {
    for (city in cities){
        val food = getFoodByCity(city)
        send("$food desde $city")
    }

}

suspend fun getFoodByCity(city: String): String {
    delay(300)
    return when(city){
        "Santander" -> "Arepa"
        "CDMX" -> "Taco"
        "Lima" -> "Ceviche"
        "Buenos Aires" -> "Asado"
        "Santiago" -> "Mote con huesillo"
        else -> "Sin datos"
    }

}

fun produceChannel() {
    runBlocking {
        newTopic("Canales y el patrón productor/consumidor")

        val names = produceCities()
        names.consumeEach {
            println(it)
        }
    }
}

fun CoroutineScope.produceCities(): ReceiveChannel<String> = produce {
    countries.forEach{
        send(it)
    }
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
