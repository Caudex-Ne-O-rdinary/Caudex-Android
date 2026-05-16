package com.cmc.caudex.data.remote.api

import com.cmc.caudex.data.remote.model.ApiResponse
import com.cmc.caudex.data.remote.model.PlantDetailResponse
import com.cmc.caudex.data.remote.model.PlantUploadResponse
import com.cmc.caudex.data.remote.model.WriteDiaryRequest
import com.cmc.caudex.data.remote.model.WriteDiaryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PlantApi {

    @Multipart
    @POST("api/plants")
    suspend fun uploadPlant(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("managementTip") managementTip: RequestBody,
        @Part("ratioX") ratioX: RequestBody,
        @Part("ratioY") ratioY: RequestBody,
    ): ApiResponse<PlantUploadResponse>

    @GET("api/plants/{plantId}")
    suspend fun getPlant(
        @Path("plantId") plantId: Int,
    ): ApiResponse<PlantDetailResponse>

    @POST("api/plants/{plantId}/diary")
    suspend fun writeDiary(
        @Path("plantId") plantId: Int,
        @Body request: WriteDiaryRequest,
    ): ApiResponse<WriteDiaryResponse>

}
