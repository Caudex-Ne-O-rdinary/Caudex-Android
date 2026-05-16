package com.cmc.caudex.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class CaudexApiResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T,
)
