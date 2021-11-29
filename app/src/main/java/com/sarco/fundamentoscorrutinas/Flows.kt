package com.sarco.fundamentoscorrutinas

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.Exception
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
//    coldFLow()
//    cancelFLow()
//    flowOperators()
//    terminalFlowOperators()
//    bufferFlow()
//    conflationFlow()
//    multiFlow()
//    flatFlows()
//    flowExceptions()
    completions()

}

fun completions() {
    runBlocking {
        newTopic("Fin de un flujo(onCompletion)")
        getCitiesFlow()
            .onCompletion {
                println("Quitar el progress bar")
            }
//            .collect { println(it) }

        println()

        getMatchResultsFlow()
            .onCompletion {
                println("Mostrar las estadisticas")
            }
            .catch { emit("Error: $this") }
//            .collect { println(it) }

        newTopic("Cancelar Flow")
        getDataByFlowStatic()
            .onCompletion {
                println("Ya no le interesa al usuario")
            }
            .cancellable()
            .collect {
                if(it > 22.5f) cancel()
                println(it)
            }
    }
}


fun flowExceptions() {
    runBlocking {
        newTopic("Control de errores")
        newTopic("Try/Catch")
//        try {
//            getMatchResultsFlow()
//                .collect {
//                    println(it)
//                    if(it.contains("2")) throw Exception("Habian acordado 1-1 :V")
//                }
//        }catch (e: Exception){
//            e.printStackTrace()
//        }

        newTopic("Transparencia")
        getMatchResultsFlow()
            .catch {
                emit("Error: $this")
            }
            .collect {
                println(it)
                if(!it.contains("-")) println("Notifica al programador")
            }
    }
}


fun flatFlows() {
    runBlocking {
        newTopic("Flujos de aplanamiento")
        newTopic("Flat Map Concat")
        getCitiesFlow()
            .flatMapConcat { city ->
                getDataToFlatFlow(city)
            }
            .map { setFormat(it) }
            .collect { println(it) }
        newTopic("Flat Map Merge")
        getCitiesFlow()
            .flatMapMerge { city ->
                getDataToFlatFlow(city)
            }
            .map { setFormat(it) }
            .collect { println(it) }

    }
}

fun getDataToFlatFlow(city: String): Flow<Float>  = flow {
    (1..3).forEach {
        println("Temperatura de Ayer en $city...")
        emit(Random.nextInt(10, 30).toFloat())
        println("Temperatura Actual en  $city...")
        delay(100)
        emit(20 + it + Random.nextFloat())
    }
}

fun getCitiesFlow(): Flow<String> = flow {
    listOf("Santander", "CDMX", "Lima")
        .forEach{ city ->
            println("\nConsultando ciudad...")
            delay(1_000)
            emit(city)
        }
}

fun multiFlow() {
    runBlocking {
        newTopic("Zip & Combine")
        getDataByFlowStatic()
            .map { setFormat(it) }
            .combine(getMatchResultsFlow()){ degrees, result ->
                "$result with $degrees"
            }
//            .zip(getMatchResultsFlow()) { degrees, result ->
//                "$result with $degrees"
//            }
            .collect { println(it) }
    }
}


fun conflationFlow() {
    runBlocking { 
        newTopic("Fusión")
        val time = measureTimeMillis { 
            getMatchResultsFlow()
                .conflate()
//                .buffer()
                .collectLatest {
                    delay(100)
                    println(it)
                }
//                .collect {
//                    delay(100)
//                    println(it)
//                }
        }
        println("Time: ${time}ms")
    }
}

fun getMatchResultsFlow(): Flow<String> {
    return flow { 
        var homeTeam = 0
        var awayTeam = 0
        (0..45).forEach { 
            println("Minuto: $it")
            delay(50)
            homeTeam += Random.nextInt(0, 21)/20
            awayTeam += Random.nextInt(0, 21)/20
            emit("$homeTeam - $awayTeam")

            if(homeTeam == 2 || awayTeam == 2) throw Exception("Habian acordado 1 y 1 :V")
        }
    }
}


fun bufferFlow() {
    runBlocking {
        newTopic("BUffer para flow")
        val time = measureTimeMillis {
            getDataByFlowStatic()
            .map {
                setFormat(it)
            }
            .buffer()
            .collect {              //000111222333444
                delay(500) // 0000011111222223333344444
                println(it)
            }
        }

        println("Time: ${time}ms")
    }
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

        val totalSaving = getDataByFlow()
            .fold(saving) { acc, value ->
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
