package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.model.Garden
import com.cmc.caudex.domain.repository.IGardenRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class CreateGardenUseCase @Inject constructor(
    private val gardenRepository: IGardenRepository,
) {
    suspend operator fun invoke(
        name: String,
        templateId: Int,
    ): Result<Garden> =
        runSuspendCatching {
            val trimmedName = name.trim()
            require(trimmedName.isNotEmpty()) { "이름을 입력해주세요." }
            gardenRepository.createGarden(name = trimmedName, templateId = templateId)
        }
}
