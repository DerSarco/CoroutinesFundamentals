package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.*

fun main() {
//    globalScope()w

//        suspendFun()

    newTopic("Constructores de corrutinas")
//    cRunBlocking(
//    cLaunch()
    cAsync()
//    esta linea espera que reciba un carácter desde consola.
    readLine()
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
