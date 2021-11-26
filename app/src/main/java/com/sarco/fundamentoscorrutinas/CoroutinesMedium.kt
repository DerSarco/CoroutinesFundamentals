package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

fun main() {
//    dispatchers()
//    nested()
//    changeWithContext()
    basicFlows()
}

fun basicFlows() {
    newTopic("Flows Basicos")
   runBlocking {
       launch {
           getDataByFlow().collect { println(it) }
       }

       launch {
           (1..50).forEach {
               delay(someTime()/10)
               println("Tarea 2")
           }
       }
   }
}

fun getDataByFlow(): Flow<Float> {
    return flow {
        (1..5).forEach {
            println("Procesando datos...")
            delay(someTime())
            emit(20 + it + Random.nextFloat())
        }
    }
}

fun changeWithContext() {
    runBlocking {
        newTopic("withContext")
        startMsg()

        withContext(newSingleThreadContext("Cursos Android Ant")){
            startMsg()

            delay(someTime())
            println("CursosAndroidAnt")

            endMsg()
        }
        withContext(Dispatchers.IO){
            startMsg()

            delay(someTime())
            println("Peticion al servidor.")

            endMsg()
        }

        endMsg()

    }
}


fun nested() {
    runBlocking {
        newTopic("Anidar")
        val job = launch {
            startMsg()
            launch {
                startMsg()

                delay(someTime())
                println("Otra tarea")
                endMsg()
            }
            val subJob = launch(Dispatchers.IO) {
                startMsg()
                launch(newSingleThreadContext("Cursos Android ANt")) {
                    startMsg()
                    println("Otra tarea cursos android ant")
                    endMsg()
                }
                println("Se re Cancelo...")
                delay(someTime())
                println("Tarea en el servidor")
                endMsg()
            }
            delay(someTime()/4)
            subJob.cancel()
            var sum = 0
            (1..100).forEach{
                sum += it
                delay(someTime()/100)
            }
            println("Sumn: $sum")
            endMsg()
        }
        delay(someTime()/2)
        job.cancel()
        println("Job Cancelado...")
    }
}


fun dispatchers() {
    runBlocking {
        newTopic("Dispatchers")
        launch {
            startMsg()

            println("None")

            endMsg()
        }

        launch(Dispatchers.IO) {
            startMsg()

            println("IO")

            endMsg()
        }
        launch(Dispatchers.Unconfined) {
            startMsg()

            println("Unconfined")

            endMsg()
        }

//        Main Solo para Android.
        launch(Dispatchers.Default) {
            startMsg()

            println("Default")

            endMsg()
        }
        launch(newSingleThreadContext("Cursos Android Ant")) {
            startMsg()

            println("Mi COrrutina personalizada con un dispatcher")

            endMsg()
        }

        newSingleThreadContext("CursosAndroidAnt").use { myContext ->
            launch(myContext) {

                startMsg()

                println("Mi COrrutina personalizada con un dispatcher 2")

                endMsg()
            }
        }
    }
}
