package com.cmc.caudex.data.repository

import com.cmc.caudex.data.remote.api.PlantApiService
import com.cmc.caudex.domain.model.PlantDetail
import com.cmc.caudex.domain.repository.IPlantRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class PlantRepository @Inject constructor(
    private val api: PlantApiService,
) : IPlantRepository {

    override suspend fun uploadPlant(
        imageFile: File,
        name: String,
        managementTip: String,
        ratioX: Double,
        ratioY: Double,
    ): Int {
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = imageFile.name,
            body = imageFile.asRequestBody("image/*".toMediaType()),
        )
        return api.uploadPlant(
            image = imagePart,
            name = name.toRequestBody("text/plain".toMediaType()),
            managementTip = managementTip.toRequestBody("text/plain".toMediaType()),
            ratioX = ratioX.toString().toRequestBody("text/plain".toMediaType()),
            ratioY = ratioY.toString().toRequestBody("text/plain".toMediaType()),
        ).result.plantId
    }

    override suspend fun getPlant(plantId: Int): PlantDetail = TODO("미구현")

    override suspend fun writeDiary(plantId: Int, content: String): Int = TODO("미구현")
}
