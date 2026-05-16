package com.cmc.caudex.data.remote.api

import com.cmc.caudex.data.remote.dto.response.CaudexApiResponse
import com.cmc.caudex.data.remote.dto.response.PlantUploadResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PlantApiService {

    @Multipart
    @POST("api/plants")
    suspend fun uploadPlant(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("managementTip") managementTip: RequestBody,
        @Part("gardenId") gardenId: RequestBody,
        @Part("ratioX") ratioX: RequestBody,
        @Part("ratioY") ratioY: RequestBody,
        @Part("scale") scale: RequestBody,
    ): CaudexApiResponse<PlantUploadResult>
}
