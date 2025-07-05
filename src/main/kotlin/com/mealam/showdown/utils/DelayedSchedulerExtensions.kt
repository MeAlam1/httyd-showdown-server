package com.mealam.showdown.utils

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

fun Runnable.schedule(
    executor: ScheduledExecutorService,
    delay: Long,
    unit: TimeUnit
): CompletableFuture<Void> {
    val future = CompletableFuture<Void>()
    executor.schedule({
        try {
            this.run()
            future.complete(null)
        } catch (e: Exception) {
            future.completeExceptionally(e)
        }
    }, delay, unit)
    return future
}

fun <A> (() -> A).schedule(
    executor: ScheduledExecutorService,
    delay: Long,
    unit: TimeUnit
): CompletableFuture<A> {
    val future = CompletableFuture<A>()
    executor.schedule({
        try {
            val result = this()
            future.complete(result)
        } catch (e: Exception) {
            future.completeExceptionally(e)
        }
    }, delay, unit)
    return future
}