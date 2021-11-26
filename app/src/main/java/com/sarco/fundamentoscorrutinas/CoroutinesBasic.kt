package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

fun main() {
//    globalScope()w

//        suspendFun()

    newTopic("Constructores de corrutinas")
//    cRunBlocking(
//    cLaunch()
//    cAsync()
//    job()
//    deferred()
    cProduce()

//    esta linea espera que reciba un carácter desde consola.
    readLine()
}

fun cProduce()  = runBlocking {
    newTopic("Produce")
    val names = produceNames()
    names.consumeEach { println(it)}
}

fun CoroutineScope.produceNames(): ReceiveChannel<String> = produce {
    (1..5).forEach{ send("name$it")}
}

fun deferred() {
    runBlocking {
        newTopic("Deferred")

        val deferred = async {
            startMsg()
            delay(someTime())
            println("Deferred...")
            endMsg()
            multi(5,2)
        }

        println("Deferred: $deferred")
        println("Valor del Deferred.await: ${deferred.await()}")


        val result = async {
            multi(3,3)
        }.await()

        println(result)
    }
}

fun job() {
    runBlocking {
        newTopic("Job")
        val job = launch {
            startMsg()
            delay(2_100)
            println("Job executed")
            endMsg()
        }
        println("Job: $job")
        println("isActive: ${job.isActive}")
        println("isCancelled: ${job.isCancelled}")
        println("isCompleted: ${job.isCompleted}")

        delay(someTime())
        println("Tarea cancelada")
        job.cancel()

        println("isActive: ${job.isActive}")
        println("isCancelled: ${job.isCancelled}")
        println("isCompleted: ${job.isCompleted}")
    }
}

fun cAsync() {
    newTopic("Async")
    runBlocking {
        newTopic("Async...")
        val result = async {
            startMsg()
            println("Async executed")
            endMsg()
            1
        }

        println("Result ${result.await()}")
    }
}

fun cLaunch() {
    runBlocking {
        newTopic("Launch")
        launch {
            startMsg()
            delay(someTime())
            println("Launch...")
            endMsg()

        }
    }
}

fun cRunBlocking() {
    newTopic("RunBlocking")
    runBlocking {
        startMsg()

        delay(someTime())
        println("runBlocking...")

        endMsg()
    }
}

fun suspendFun() {
    newTopic("Suspend")
    Thread.sleep(someTime())
    GlobalScope.launch {
        delay(someTime())
    }
}

fun globalScope() {
    newTopic("GlobalScope")
    GlobalScope.launch {

        startMsg()
        println("Mi Corrutina")
        endMsg()
    }
}

fun startMsg() {
    println("Comenzando corrutina - ${Thread.currentThread().name}-")
}

fun endMsg() {
    println("Corrutina - ${Thread.currentThread().name}-- Finalizada")
}
