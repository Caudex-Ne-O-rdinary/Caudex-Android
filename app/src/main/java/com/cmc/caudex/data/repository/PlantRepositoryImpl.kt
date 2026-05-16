package com.cmc.caudex.data.repository

import com.cmc.caudex.data.remote.api.PlantApi
import com.cmc.caudex.data.remote.model.PlantUploadRequest
import com.cmc.caudex.data.remote.model.WriteDiaryRequest
import com.cmc.caudex.data.remote.model.toDomain
import com.cmc.caudex.domain.model.PlantDetail
import com.cmc.caudex.domain.repository.IPlantRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantApi: PlantApi,
    private val json: Json,
) : IPlantRepository {

    override suspend fun uploadPlant(
        gardenId: String,
        imageFile: File,
        name: String,
        managementTip: String,
        ratioX: Double,
        ratioY: Double,
        scale: Int,
    ): Int {
        val response = plantApi.uploadPlant(
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = imageFile.name,
                body = imageFile.asRequestBody("image/*".toMediaType()),
            ),
            dto = json.encodeToString(
                PlantUploadRequest(
                    gardenId = gardenId,
                    name = name,
                    managementTip = managementTip,
                    ratioX = ratioX,
                    ratioY = ratioY,
                    scale = scale,
                )
            ).toRequestBody("application/json".toMediaType()),
        )
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.plantId
    }

    override suspend fun getPlant(plantId: Int): PlantDetail {
        val response = plantApi.getPlant(plantId)
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.toDomain()
    }

    override suspend fun writeDiary(plantId: Int, content: String): Int {
        val response = plantApi.writeDiary(plantId, WriteDiaryRequest(content = content))
        val result = response.result
        if (!response.isSuccess || result == null) error(response.message)
        return result.diaryId
    }
}
