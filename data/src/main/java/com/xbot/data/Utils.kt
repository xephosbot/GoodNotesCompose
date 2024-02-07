package com.xbot.data

fun <T> List<T>.prepend(element: T): List<T> {
    return listOf(element) + this
}
