package com.cmc.caudex.data.remote.model

import com.cmc.caudex.domain.model.Garden
import com.cmc.caudex.domain.model.GardenDetail
import com.cmc.caudex.domain.model.GardenTemplate
import com.cmc.caudex.domain.model.DEFAULT_PLANT_SCALE
import com.cmc.caudex.domain.model.Plant
import kotlinx.serialization.Serializable

@Serializable
data class CreateGardenRequest(
    val name: String,
    val templateId: Int,
)

@Serializable
data class GardenResponse(
    val gardenId: String = "",
    val gardenUrl: String = "",
    val name: String = "",
    val templateUrl: String = "",
    val createdAt: String = "",
)

fun GardenResponse.toDomain(): Garden =
    Garden(
        gardenId = gardenId,
        gardenUrl = gardenUrl,
        name = name,
        templateUrl = templateUrl,
        createdAt = createdAt,
    )

// --- 1. 정원 생성 화면 템플릿 목록 ---

@Serializable
data class GardenTemplateItemResponse(
    val templateId: Int,
    val name: String,
    val imageUrl: String,
)

@Serializable
data class GardenTemplatesResponse(
    val templates: List<GardenTemplateItemResponse>,
)

fun GardenTemplateItemResponse.toDomain(): GardenTemplate =
    GardenTemplate(templateId = templateId, name = name, imageUrl = imageUrl)

// --- 2. 정원 조회 ---

@Serializable
data class PlantResponse(
    val plantId: Int,
    val ratioX: Double,
    val ratioY: Double,
    val plantUrl: String? = null,
    val imageUrl: String? = null,
    val scale: Int = DEFAULT_PLANT_SCALE,
)

@Serializable
data class GardenDetailResponse(
    val templateUrl: String,
    val plants: List<PlantResponse>,
)

fun PlantResponse.toDomain(): Plant =
    Plant(
        plantId = plantId,
        ratioX = ratioX,
        ratioY = ratioY,
        plantUrl = plantUrl.orEmpty().ifBlank { imageUrl.orEmpty() },
        scale = scale,
    )

fun GardenDetailResponse.toDomain(): GardenDetail =
    GardenDetail(templateUrl = templateUrl, plants = plants.map { it.toDomain() })

// --- 3. 정원 식물 배치 수정 ---

@Serializable
data class UpdatePlantPositionRequest(
    val plantId: Int,
    val ratioX: Double,
    val ratioY: Double,
    val scale: Int,
)

@Serializable
data class GardenModifiedResponse(
    val modifiedAt: String,
)
