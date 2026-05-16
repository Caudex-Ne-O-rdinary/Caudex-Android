package com.cmc.caudex.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.caudex.data.local.GardenPreferencesDataSource
import com.cmc.caudex.domain.usecase.GetGardenTemplatesUseCase
import com.cmc.caudex.presentation.navigation.GardenInviteLink
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getGardenTemplatesUseCase: GetGardenTemplatesUseCase,
    private val gardenPreferencesDataSource: GardenPreferencesDataSource,
) : ViewModel() {

    sealed interface Destination {
        data object RoomCreate : Destination
        data object Room : Destination
    }

    private val _destination = MutableSharedFlow<Destination>()
    val destination = _destination.asSharedFlow()

    init {
        viewModelScope.launch {
            // 스플래시 대기 중 템플릿 미리 로드
            launch { getGardenTemplatesUseCase() }
            delay(1700L)
            val gardenId = gardenPreferencesDataSource.getGardenIdOnce()
                .ifBlank { GardenInviteLink.extractGardenId(gardenPreferencesDataSource.getGardenUrlOnce()) }
            val hasGarden = gardenId.isNotBlank()
            _destination.emit(if (hasGarden) Destination.Room else Destination.RoomCreate)
        }
    }
}
