package com.cmc.caudex.presentation.room

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.caudex.data.local.GardenPreferencesDataSource
import com.cmc.caudex.domain.model.DEFAULT_PLANT_SCALE
import com.cmc.caudex.domain.model.Plant
import com.cmc.caudex.domain.usecase.GetGardenUseCase
import com.cmc.caudex.presentation.navigation.GardenInviteLink
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
class RoomViewModel @Inject constructor(
    private val getGardenUseCase: GetGardenUseCase,
    private val gardenPreferencesDataSource: GardenPreferencesDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RoomEffect>()
    val effect = _effect.asSharedFlow()

    private var loadGardenJob: Job? = null

    init {
        loadGarden()
    }

    fun loadGarden(force: Boolean = false) {
        if (!force && _uiState.value.isLoading) return

        if (force) loadGardenJob?.cancel()
        loadGardenJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val savedLink = gardenPreferencesDataSource.getGardenUrlOnce()
            val gardenId = gardenPreferencesDataSource.getGardenIdOnce()
                .ifBlank { GardenInviteLink.extractGardenId(savedLink) }
            _uiState.update { it.copy(gardenId = gardenId) }
            if (gardenId.isBlank()) {
                val templateUrl = gardenPreferencesDataSource.getTemplateUrlOnce()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        gardenImageUrl = templateUrl,
                        plants = emptyList(),
                    )
                }
                return@launch
            }

            getGardenUseCase(gardenId)
                .onSuccess { garden ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            gardenId = gardenId,
                            gardenImageUrl = garden.templateUrl,
                            plants = garden.plants.map { it.toUiModel() },
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    val templateUrl = gardenPreferencesDataSource.getTemplateUrlOnce()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            gardenImageUrl = templateUrl,
                            plants = emptyList(),
                            errorMessage = throwable.message ?: "정원을 불러오지 못했어요",
                        )
                    }
                }
        }
    }

    fun refreshGarden() {
        loadGarden(force = true)
    }

    fun onUploadButtonClick() {
        viewModelScope.launch {
            val savedLink = gardenPreferencesDataSource.getGardenUrlOnce()
            val gardenId = gardenPreferencesDataSource.getGardenIdOnce()
                .ifBlank { GardenInviteLink.extractGardenId(savedLink) }
            _effect.emit(RoomEffect.CopyLinkToClipboard(GardenInviteLink.fromSavedLinkOrId(savedLink, gardenId)))
        }
    }

    fun onPlantClick(plantId: Int) {
        viewModelScope.launch {
            val myPlantId = gardenPreferencesDataSource.getUploadedPlantIdOnce()
            _effect.emit(RoomEffect.NavigateToPlantFriend(plantId, isMine = myPlantId != 0 && plantId == myPlantId))
        }
    }

    private fun Plant.toUiModel(): RoomPlantUiModel =
        RoomPlantUiModel(
            plantId = plantId,
            imageUrl = plantUrl,
            ratioX = ratioX.coerceIn(0.0, 1.0),
            ratioY = ratioY.coerceIn(0.0, 1.0),
            scalePx = scale.takeIf { it > 0 } ?: DEFAULT_PLANT_SCALE,
        )
}

@Immutable
data class RoomUiState(
    val isLoading: Boolean = false,
    val gardenId: String = "",
    val gardenImageUrl: String = "",
    val plants: List<RoomPlantUiModel> = emptyList(),
    val errorMessage: String? = null,
)

@Immutable
data class RoomPlantUiModel(
    val plantId: Int,
    val imageUrl: String,
    val ratioX: Double,
    val ratioY: Double,
    val scalePx: Int = DEFAULT_PLANT_SCALE,
)

sealed interface RoomEffect {
    data class CopyLinkToClipboard(val link: String) : RoomEffect
    data class NavigateToPlantFriend(val plantId: Int, val isMine: Boolean) : RoomEffect
}
