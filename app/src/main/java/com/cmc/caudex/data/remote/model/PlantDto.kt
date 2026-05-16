package com.cmc.caudex.data.remote.model

import com.cmc.caudex.domain.model.Diary
import com.cmc.caudex.domain.model.PlantDetail
import kotlinx.serialization.Serializable

@Serializable
data class PlantUploadResponse(
    val plantId: Int,
)

@Serializable
data class DiaryItemResponse(
    val diaryId: Int,
    val content: String,
    val createdAt: String,
)

@Serializable
data class PlantDetailResponse(
    val plantId: Int,
    val name: String,
    val imageUrl: String,
    val managementTip: String,
    val diaries: List<DiaryItemResponse>,
)

@Serializable
data class WriteDiaryRequest(
    val content: String,
)

@Serializable
data class WriteDiaryResponse(
    val diaryId: Int,
)

fun DiaryItemResponse.toDomain() = Diary(
    diaryId = diaryId,
    content = content,
    createdAt = createdAt,
)

fun PlantDetailResponse.toDomain() = PlantDetail(
    plantId = plantId,
    name = name,
    imageUrl = imageUrl,
    managementTip = managementTip,
    diaries = diaries.map { it.toDomain() },
)
