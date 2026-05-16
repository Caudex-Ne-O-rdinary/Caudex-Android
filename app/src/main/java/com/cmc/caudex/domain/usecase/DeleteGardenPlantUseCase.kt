package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.repository.IGardenRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class DeleteGardenPlantUseCase @Inject constructor(
    private val gardenRepository: IGardenRepository,
) {
    suspend operator fun invoke(gardenId: String, plantId: Int): Result<String> =
        runSuspendCatching { gardenRepository.deletePlant(gardenId, plantId) }
}
