package com.cmc.caudex.data.repository

import com.cmc.caudex.data.remote.api.GardenApi
import com.cmc.caudex.data.remote.model.CreateGardenRequest
import com.cmc.caudex.data.remote.model.UpdatePlantPositionRequest
import com.cmc.caudex.data.remote.model.toDomain
import com.cmc.caudex.domain.model.Garden
import com.cmc.caudex.domain.model.GardenDetail
import com.cmc.caudex.domain.model.GardenTemplate
import com.cmc.caudex.domain.repository.IGardenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GardenRepositoryImpl @Inject constructor(
    private val gardenApi: GardenApi,
) : IGardenRepository {

    private var cachedTemplates: List<GardenTemplate>? = null

    override suspend fun createGarden(
        name: String,
        templateId: Int,
    ): Garden {
        val response = gardenApi.createGarden(CreateGardenRequest(name = name, templateId = templateId))
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.toDomain()
    }

    override suspend fun getGardenTemplates(): List<GardenTemplate> {
        cachedTemplates?.let { return it }
        val response = gardenApi.getGardenTemplates()
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.templates.map { it.toDomain() }.also { cachedTemplates = it }
    }

    override suspend fun getGarden(gardenId: String): GardenDetail {
        val response = gardenApi.getGarden(gardenId)
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.toDomain()
    }

    override suspend fun updatePlantPosition(
        gardenId: String,
        plantId: Int,
        ratioX: Double,
        ratioY: Double,
        scale: Int,
    ): String {
        val response = gardenApi.updatePlantPosition(
            gardenId = gardenId,
            request = UpdatePlantPositionRequest(plantId = plantId, ratioX = ratioX, ratioY = ratioY, scale = scale),
        )
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.modifiedAt
    }

}
