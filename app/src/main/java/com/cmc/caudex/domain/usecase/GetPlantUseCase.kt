package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.model.PlantDetail
import com.cmc.caudex.domain.repository.IPlantRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class GetPlantUseCase @Inject constructor(
    private val plantRepository: IPlantRepository,
) {
    suspend operator fun invoke(plantId: Int): Result<PlantDetail> =
        runSuspendCatching { plantRepository.getPlant(plantId) }
}
