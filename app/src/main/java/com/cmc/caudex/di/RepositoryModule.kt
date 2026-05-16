package com.cmc.caudex.di

import com.cmc.caudex.data.repository.GardenRepositoryImpl
import com.cmc.caudex.data.repository.PlantRepositoryImpl
import com.cmc.caudex.domain.repository.IGardenRepository
import com.cmc.caudex.domain.repository.IPlantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGardenRepository(
        gardenRepositoryImpl: GardenRepositoryImpl,
    ): IGardenRepository

    @Binds
    abstract fun bindPlantRepository(
        plantRepositoryImpl: PlantRepositoryImpl,
    ): IPlantRepository

}

