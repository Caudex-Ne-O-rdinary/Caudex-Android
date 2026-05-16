package com.cmc.caudex.presentation.room.guest

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cmc.caudex.domain.model.Plant
import com.cmc.caudex.domain.usecase.GetGardenUseCase
import com.cmc.caudex.presentation.navigation.GardenInviteLink
import com.cmc.caudex.presentation.navigation.GuestRoomRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuestRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGardenUseCase: GetGardenUseCase,
) : ViewModel() {

    private val gardenId: String = savedStateHandle.toRoute<GuestRoomRoute>().gardenId

    private val _uiState = MutableStateFlow(GuestRoomUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GuestRoomEffect>()
    val effect = _effect.asSharedFlow()

    private var loadGardenJob: Job? = null

    init {
        loadGarden()
    }

    fun retryLoad() = loadGarden(force = true)

    fun refreshGarden() = loadGarden(force = true)

    fun onShareClick() {
        viewModelScope.launch {
            _effect.emit(GuestRoomEffect.CopyLinkToClipboard(GardenInviteLink.fromGardenId(gardenId)))
        }
    }

    fun onPlantClick(plantId: Int) {
        viewModelScope.launch {
            _effect.emit(GuestRoomEffect.NavigateToPlantFriend(plantId, isMine = false))
        }
    }

    private fun loadGarden(force: Boolean = false) {
        if (!force && _uiState.value.isLoading) return
        if (force) loadGardenJob?.cancel()

        loadGardenJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getGardenUseCase(gardenId)
                .onSuccess { garden ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            gardenImageUrl = garden.templateUrl,
                            plants = garden.plants.map { p -> p.toUiModel() },
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "정원을 불러오지 못했어요",
                        )
                    }
                }
        }
    }

    private fun Plant.toUiModel() = GuestPlantUiModel(
        plantId = plantId,
        imageUrl = plantUrl,
        ratioX = ratioX.coerceIn(0.0, 1.0),
        ratioY = ratioY.coerceIn(0.0, 1.0),
        scalePx = scale.takeIf { it > 0 } ?: DEFAULT_PLANT_SCALE_PX_FALLBACK,
    )

    private companion object {
        const val DEFAULT_PLANT_SCALE_PX_FALLBACK = 80
    }
}

@Immutable
data class GuestRoomUiState(
    val isLoading: Boolean = false,
    val gardenImageUrl: String = "",
    val plants: List<GuestPlantUiModel> = emptyList(),
    val errorMessage: String? = null,
)

@Immutable
data class GuestPlantUiModel(
    val plantId: Int,
    val imageUrl: String,
    val ratioX: Double,
    val ratioY: Double,
    val scalePx: Int = 80,
)

sealed interface GuestRoomEffect {
    data class NavigateToPlantFriend(val plantId: Int, val isMine: Boolean) : GuestRoomEffect
    data class CopyLinkToClipboard(val link: String) : GuestRoomEffect
}
