package com.cmc.caudex.domain.usecase

import com.cmc.caudex.domain.repository.IPlantRepository
import com.cmc.caudex.domain.util.runSuspendCatching
import javax.inject.Inject

class WriteDiaryUseCase @Inject constructor(
    private val plantRepository: IPlantRepository,
) {
    suspend operator fun invoke(plantId: Int, content: String): Result<Int> =
        runSuspendCatching { plantRepository.writeDiary(plantId, content) }
}
