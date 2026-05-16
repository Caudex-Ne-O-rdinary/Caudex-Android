package com.cmc.caudex.data.repository.fake

import com.cmc.caudex.domain.model.Diary
import com.cmc.caudex.domain.model.PlantDetail
import com.cmc.caudex.domain.repository.IPlantRepository
import java.io.File
import javax.inject.Inject

class FakePlantRepository @Inject constructor() : IPlantRepository {

    override suspend fun uploadPlant(
        imageFile: File,
        name: String,
        managementTip: String,
        ratioX: Double,
        ratioY: Double,
    ): Int = 1

    override suspend fun getPlant(plantId: Int): PlantDetail = PlantDetail(
        plantId = plantId,
        name = "몬스테라",
        imageUrl = "https://picsum.photos/seed/plant$plantId/300/300",
        managementTip = "햇빛이 잘 드는 곳에 두고 주 1회 물을 주세요.",
        diaries = listOf(
            Diary(diaryId = 1, content = "오늘 새 잎이 났어요!", createdAt = "2026-05-10T10:00:00"),
            Diary(diaryId = 2, content = "물을 줬습니다.", createdAt = "2026-05-17T09:00:00"),
        ),
    )

    override suspend fun writeDiary(plantId: Int, content: String): Int = 1
}
