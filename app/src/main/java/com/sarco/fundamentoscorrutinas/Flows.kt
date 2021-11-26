package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
//    coldFLow()
//    cancelFLow()
//    flowOperators()
    terminalFlowOperators()
}

fun terminalFlowOperators() {
    runBlocking {
        newTopic("Operadores Flow terminales")
        newTopic("List")

        val list = getDataByFlow()
//            .toList()
        println("List: $list")

        newTopic("Single")

        val single = getDataByFlow()
//            .take(1)
//            .single()
        println("Single: $single")

        newTopic("First")
        val first = getDataByFlow()
//            .first()
        println("First: $first")

        newTopic("Last")
        val last = getDataByFlow()
//            .last()
        println("Last: $last")

        newTopic("Reduce")
        val saving = getDataByFlow()
            .reduce { accumulator, value ->
            println("Acumulator: $accumulator")
            println("Value: $value")
            println("Current saving = ${accumulator + value} ")
            accumulator + value
        }
        println("Saving: $saving")

        newTopic("Fold")

        val lastSaving = saving
        val totalSaving = getDataByFlow()
            .fold(lastSaving) {acc, value ->
                println("Acumulator: $acc")
                println("Value: $value")
                println("Current saving = ${acc + value} ")
                acc + value
            }
        println("Total Saving: $totalSaving")
    }
}


fun flowOperators() {
    runBlocking {
        newTopic("Operadores Flow Intermediarios.")
        newTopic("Map")
        getDataByFlow().map {
            setFormat(it)
            setFormat(convertCelsToFahr(it), "F")
        }
//        }.collect {
//            println(it)
//        }
        newTopic("Filter")
        getDataByFlow().filter {
            it < 23
        }.map {
            setFormat(it)
        }
//        .collect {
//            println(it)
//        }

        newTopic("Transform")
        getDataByFlow()
            .transform {
                emit(setFormat(it))
                emit(setFormat(convertCelsToFahr(it), "F"))
            }
//            .collect { println(it) }

        newTopic("Take")
        getDataByFlow()
            .take(3)
            .map {
                setFormat(it)
            }
            .collect { println(it) }
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
