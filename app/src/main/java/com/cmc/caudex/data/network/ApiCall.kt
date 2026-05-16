package com.cmc.caudex.data.network

import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
    runCatching { block() }
        .onFailure { if (it is CancellationException) throw it }
