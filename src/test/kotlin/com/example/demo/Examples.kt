package com.example.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.flux
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import kotlin.coroutines.cancellation.CancellationException

class Examples {

    @Test
    fun example1() {
        data class User(
            val id: Long,
            val email: String? = null,
        )

        fun greet(u: User): String = "Szia, ${(u.email?.substringBefore("@") ?: "ismeretlen")}"

        println(greet(User(1, "John")))
        println(
            greet(User(id = 1))
        )
    }

    @Test
    fun smartcast() {
        val name: String? = null

        if (name != null) {
            name.lowercase()
        }
    }

    @Test
    fun trailingLambda() {
        fun fetchData(url: String, onSuccess: (String) -> Unit) {
            println("Fetching from $url...")
            val mockData = "some data"
            onSuccess(mockData)
        }

        fetchData("https://some.url") {
            println("Received: $it")
        }
        fetchData(
            "https://some.url",
            onSuccess = { println("Received: $it") }
        )
    }

    @Test
    fun listAndCopy() {
        data class Order(
            val id: Long,
            val items: List<String>
        )

        val o1 = Order(1, listOf("A", "B"))
        val o2 = o1.copy(items = o1.items + "C")

        val mutableList: MutableList<String> = mutableListOf()
        mutableList += "x"

        val hashMapOf = hashMapOf(1 to null, 2 to "valami")
    }

    @Test
    fun extensionFunction() {
        fun String.redactEmail(): String = replace(Regex("([\\w.%-]+)@"), "***@")

        val s = "alice@example.com".redactEmail()
        println(s)
    }

    @Test
    suspend fun tryCatch() {
        suspend fun errorFun() {
            throw RuntimeException("hiba")
        }

        try {
            errorFun()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("error: ${e.message}")
        }
    }

    @Test
    suspend fun interop() {
        flux {
            send(1)
            send(2)
            send(3)
        }
            .map { println(it) }
            .subscribe()

        mono { 1 }.map { println(it) }.subscribe()

        withContext(Dispatchers.IO) {
            mono { 2 }.map { println(it) }.awaitSingle()
        }

        Flux.fromIterable(listOf(1, 2, 3)).asFlow()
        flowOf(listOf(1, 2, 3)).asFlux()
    }
}