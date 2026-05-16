package com.cmc.caudex.di

import com.cmc.caudex.data.repository.fake.FakeGardenRepository
import com.cmc.caudex.data.repository.fake.FakePlantRepository
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
        fakeGardenRepository: FakeGardenRepository,
    ): IGardenRepository

    @Binds
    abstract fun bindPlantRepository(
        fakePlantRepository: FakePlantRepository,
    ): IPlantRepository

}

