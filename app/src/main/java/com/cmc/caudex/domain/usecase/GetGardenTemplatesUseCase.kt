package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.model.GardenTemplate
import com.cmc.caudex.domain.repository.IGardenRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class GetGardenTemplatesUseCase @Inject constructor(
    private val gardenRepository: IGardenRepository,
) {
    suspend operator fun invoke(): Result<List<GardenTemplate>> =
        runSuspendCatching { gardenRepository.getGardenTemplates() }
}
