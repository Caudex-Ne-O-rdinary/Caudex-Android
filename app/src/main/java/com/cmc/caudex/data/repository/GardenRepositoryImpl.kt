package com.cmc.caudex.data.repository

import com.cmc.caudex.data.remote.api.GardenApi
import com.cmc.caudex.data.remote.model.CreateGardenRequest
import com.cmc.caudex.data.remote.model.DeletePlantRequest
import com.cmc.caudex.data.remote.model.UpdatePlantPositionRequest
import com.cmc.caudex.data.remote.model.toDomain
import com.cmc.caudex.domain.model.Garden
import com.cmc.caudex.domain.model.GardenDetail
import com.cmc.caudex.domain.model.GardenTemplate
import com.cmc.caudex.domain.repository.IGardenRepository
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    private val gardenApi: GardenApi,
) : IGardenRepository {

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
        val response = gardenApi.getGardenTemplates()
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.templates.map { it.toDomain() }
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
    ): String {
        val response = gardenApi.updatePlantPosition(
            gardenId = gardenId,
            request = UpdatePlantPositionRequest(plantId = plantId, ratioX = ratioX, ratioY = ratioY),
        )
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.modifiedAt
    }

    override suspend fun deletePlant(gardenId: String, plantId: Int): String {
        val response = gardenApi.deletePlant(
            gardenId = gardenId,
            request = DeletePlantRequest(plantId = plantId),
        )
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.deletedAt
    }
}
