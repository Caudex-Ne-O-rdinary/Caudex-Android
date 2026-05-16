package com.cmc.caudex.domain.repository

import com.cmc.caudex.domain.model.Garden
import com.cmc.caudex.domain.model.GardenDetail
import com.cmc.caudex.domain.model.GardenTemplate

interface IGardenRepository {

    suspend fun createGarden(
        name: String,
        templateId: Int,
    ): Garden

    suspend fun getGardenTemplates(): List<GardenTemplate>

    suspend fun getGarden(gardenId: String): GardenDetail

    suspend fun updatePlantPosition(
        gardenId: String,
        plantId: Int,
        ratioX: Double,
        ratioY: Double,
    ): String

    suspend fun deletePlant(gardenId: String, plantId: Int): String

}
