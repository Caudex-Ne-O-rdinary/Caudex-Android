package com.cmc.caudex.data.repository.fake

import com.cmc.caudex.domain.model.Garden
import com.cmc.caudex.domain.model.GardenDetail
import com.cmc.caudex.domain.model.GardenTemplate
import com.cmc.caudex.domain.model.Plant
import com.cmc.caudex.domain.repository.IGardenRepository
import javax.inject.Inject

class FakeGardenRepository @Inject constructor() : IGardenRepository {

    override suspend fun createGarden(name: String, templateId: Int): Garden = Garden(
        gardenUrl = "https://picsum.photos/seed/garden/400/300",
        name = name,
        templateUrl = "https://picsum.photos/seed/template$templateId/400/300",
        createdAt = "2026-05-17T00:00:00",
    )

    override suspend fun getGardenTemplates(): List<GardenTemplate> = listOf(
        GardenTemplate(templateId = 1, name = "봄 정원", imageUrl = "https://picsum.photos/seed/t1/300/300"),
        GardenTemplate(templateId = 2, name = "여름 정원", imageUrl = "https://picsum.photos/seed/t2/300/300"),
        GardenTemplate(templateId = 3, name = "가을 정원", imageUrl = "https://picsum.photos/seed/t3/300/300"),
    )

    override suspend fun getGarden(gardenId: String): GardenDetail = GardenDetail(
        templateUrl = "https://picsum.photos/seed/garden$gardenId/400/300",
        plants = listOf(
            Plant(plantId = 1, ratioX = 0.3, ratioY = 0.4, plantUrl = "https://picsum.photos/seed/p1/100/100"),
            Plant(plantId = 2, ratioX = 0.6, ratioY = 0.7, plantUrl = "https://picsum.photos/seed/p2/100/100"),
        ),
    )

    override suspend fun updatePlantPosition(
        gardenId: String,
        plantId: Int,
        ratioX: Double,
        ratioY: Double,
    ): String = "success"

    override suspend fun deletePlant(gardenId: String, plantId: Int): String = "success"
}
