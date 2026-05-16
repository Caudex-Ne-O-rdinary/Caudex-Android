package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.repository.IPlantRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import java.io.File
import javax.inject.Inject

class UploadPlantUseCase @Inject constructor(
    private val plantRepository: IPlantRepository,
) {
    suspend operator fun invoke(
        imageFile: File,
        name: String,
        managementTip: String,
        ratioX: Double,
        ratioY: Double,
    ): Result<Int> =
        runSuspendCatching {
            plantRepository.uploadPlant(imageFile, name, managementTip, ratioX, ratioY)
        }
}
