package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.model.GardenDetail
import com.cmc.caudex.domain.repository.IGardenRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class GetGardenUseCase @Inject constructor(
    private val gardenRepository: IGardenRepository,
) {
    suspend operator fun invoke(gardenId: String): Result<GardenDetail> =
        runSuspendCatching { gardenRepository.getGarden(gardenId) }
}
