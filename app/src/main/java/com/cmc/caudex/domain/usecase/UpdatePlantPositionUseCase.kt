package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.repository.IGardenRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class UpdatePlantPositionUseCase @Inject constructor(
    private val gardenRepository: IGardenRepository,
) {
    suspend operator fun invoke(
        gardenId: String,
        plantId: Int,
        ratioX: Double,
        ratioY: Double,
        scale: Int,
    ): Result<String> =
        runSuspendCatching { gardenRepository.updatePlantPosition(gardenId, plantId, ratioX, ratioY, scale) }
}
