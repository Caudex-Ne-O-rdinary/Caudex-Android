package com.cmc.caudex.domain.repository

import com.cmc.caudex.domain.model.PlantDetail
import java.io.File

interface IPlantRepository {

    suspend fun uploadPlant(
        gardenId: String,
        imageFile: File,
        name: String,
        managementTip: String,
        ratioX: Double,
        ratioY: Double,
        scale: Int,
    ): Int

    suspend fun getPlant(plantId: Int): PlantDetail

    suspend fun writeDiary(plantId: Int, content: String): Int

}
