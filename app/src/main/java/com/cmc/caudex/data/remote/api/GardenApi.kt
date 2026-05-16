package com.cmc.caudex.data.remote.api

import com.cmc.caudex.data.remote.model.ApiResponse
import com.cmc.caudex.data.remote.model.CreateGardenRequest
import com.cmc.caudex.data.remote.model.DeletePlantRequest
import com.cmc.caudex.data.remote.model.GardenDetailResponse
import com.cmc.caudex.data.remote.model.GardenModifiedResponse
import com.cmc.caudex.data.remote.model.GardenPlantDeletedResponse
import com.cmc.caudex.data.remote.model.GardenResponse
import com.cmc.caudex.data.remote.model.GardenTemplatesResponse
import com.cmc.caudex.data.remote.model.UpdatePlantPositionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GardenApi {

    @POST("api/gardens")
    suspend fun createGarden(
        @Body request: CreateGardenRequest,
    ): ApiResponse<GardenResponse>

    @GET("api/gardens")
    suspend fun getGardenTemplates(): ApiResponse<GardenTemplatesResponse>

    @GET("api/gardens/{gardenId}")
    suspend fun getGarden(
        @Path("gardenId") gardenId: String,
    ): ApiResponse<GardenDetailResponse>

    @PATCH("api/gardens/{gardenId}")
    suspend fun updatePlantPosition(
        @Path("gardenId") gardenId: String,
        @Body request: UpdatePlantPositionRequest,
    ): ApiResponse<GardenModifiedResponse>

    @HTTP(method = "DELETE", path = "api/gardens/{gardenId}", hasBody = true)
    suspend fun deletePlant(
        @Path("gardenId") gardenId: String,
        @Body request: DeletePlantRequest,
    ): ApiResponse<GardenPlantDeletedResponse>

}
