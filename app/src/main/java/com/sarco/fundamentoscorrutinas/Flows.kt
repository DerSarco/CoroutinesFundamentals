package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
//    coldFLow()
//    cancelFLow()
    flowOperators()
}

fun flowOperators() {
    runBlocking {
        newTopic("Operadores Flow Intermediarios.")
        newTopic("Map")
        getDataByFlow().map {
            setFormat(it)
            setFormat(convertCelsToFahr(it), "F" )
        }.collect {
            println(it)
        }

    }
}

fun convertCelsToFahr(cels: Float): Float = ((cels * 9) / 5) + 32

fun setFormat(temp: Float, degree: String = "C"):
        String = String.format(Locale.getDefault(), "%.1f°$degree\n", temp)

fun cancelFLow() {
    runBlocking {
        newTopic("Cancelar FLow")
        val job = launch {
            getDataByFlow().collect {
                println(it)
            }
        }
        delay(someTime()*2)
        job.cancel()
    }
}


fun coldFLow() {
    newTopic("Flows are Cold.")
    runBlocking {
        val dataFLow = getDataByFlow()
        println("Esperando...")
        delay(someTime())
        dataFLow.collect {
            println(it)
        }
    }
}
