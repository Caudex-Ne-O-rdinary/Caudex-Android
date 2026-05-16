package com.cmc.caudex.presentation.plant.locate

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.caudex.domain.model.Plant
import com.cmc.caudex.domain.usecase.GetGardenUseCase
import com.cmc.caudex.domain.usecase.UpdatePlantPositionUseCase
import com.cmc.caudex.domain.usecase.UploadPlantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlantLocateViewModel @Inject constructor(
    private val getGardenUseCase: GetGardenUseCase,
    private val uploadPlantUseCase: UploadPlantUseCase,
    private val updatePlantPositionUseCase: UpdatePlantPositionUseCase,
) : ViewModel() {

    private val mode = PlantLocateMode.Create

    private val _uiState = MutableStateFlow(
        PlantLocateUiState(
            mode = mode,
            currentPlant = LocatedCurrentPlantUiModel(
                image = PlantLocateImageUiModel(
                    value = DEFAULT_IMAGE_PATH,
                    type = PlantLocateImageType.LocalPath,
                ),
                ratioX = DEFAULT_RATIO_X,
                ratioY = DEFAULT_RATIO_Y,
                scale = DEFAULT_SCALE,
            ),
        ),
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PlantLocateEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadGarden()
    }

    fun updatePlantPosition(ratioX: Double, ratioY: Double) {
        _uiState.update {
            it.copy(
                currentPlant = it.currentPlant.copy(
                    ratioX = ratioX.coerceIn(0.0, 1.0),
                    ratioY = ratioY.coerceIn(0.0, 1.0),
                ),
            )
        }
    }

    fun updatePlantScale(scale: Int) {
        _uiState.update {
            it.copy(
                currentPlant = it.currentPlant.copy(
                    scale = scale.validScaleOr(DEFAULT_SCALE),
                ),
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        if (state.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            val currentPlant = _uiState.value.currentPlant
            val result = when (mode) {
                PlantLocateMode.Create -> uploadCurrentPlant(currentPlant)
                PlantLocateMode.Edit -> updateCurrentPlantPosition(currentPlant)
            }

            result
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    _effect.emit(PlantLocateEffect.SubmitSuccess)
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = throwable.message ?: "식물 위치 저장에 실패했어요",
                        )
                    }
                }
        }
    }

    private fun loadGarden() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getGardenUseCase(DEFAULT_GARDEN_ID)
                .onSuccess { garden ->
                    val existingPlants = garden.plants
                        .map { it.toUiModel() }

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            gardenImageUrl = garden.templateUrl,
                            existingPlants = LocatedPlantCollection(existingPlants),
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "정원 정보를 불러오지 못했어요",
                        )
                    }
                }
        }
    }

    private suspend fun uploadCurrentPlant(currentPlant: LocatedCurrentPlantUiModel): Result<Int> {
        if (DEFAULT_IMAGE_PATH.isBlank()) {
            return Result.failure(IllegalStateException("등록할 식물 이미지가 없어요"))
        }

        return uploadPlantUseCase(
            imageFile = File(DEFAULT_IMAGE_PATH),
            name = DEFAULT_PLANT_NAME,
            managementTip = DEFAULT_MANAGEMENT_TIP,
            ratioX = currentPlant.ratioX,
            ratioY = currentPlant.ratioY,
        )
    }

    private suspend fun updateCurrentPlantPosition(
        currentPlant: LocatedCurrentPlantUiModel,
    ): Result<String> {
        if (DEFAULT_PLANT_ID <= 0) {
            return Result.failure(IllegalStateException("수정할 식물 정보가 없어요"))
        }

        return updatePlantPositionUseCase(
            gardenId = DEFAULT_GARDEN_ID,
            plantId = DEFAULT_PLANT_ID,
            ratioX = currentPlant.ratioX,
            ratioY = currentPlant.ratioY,
        )
    }

    private fun Plant.toUiModel(): LocatedPlantUiModel =
        LocatedPlantUiModel(
            plantId = plantId,
            imageUrl = plantUrl,
            ratioX = ratioX.coerceIn(0.0, 1.0),
            ratioY = ratioY.coerceIn(0.0, 1.0),
            scale = DEFAULT_SCALE,
        )

    private fun Double.validRatioOr(default: Double): Double =
        takeIf { it in 0.0..1.0 } ?: default

    private fun Int.validScaleOr(default: Int): Int =
        takeIf { it > 0 } ?: default

    private companion object {
        const val DEFAULT_GARDEN_ID = "1"
        const val DEFAULT_PLANT_ID = -1
        const val DEFAULT_IMAGE_PATH = ""
        const val DEFAULT_PLANT_NAME = ""
        const val DEFAULT_MANAGEMENT_TIP = ""
        const val DEFAULT_RATIO_X = 0.42
        const val DEFAULT_RATIO_Y = 0.42
        const val DEFAULT_SCALE = -1
    }
}

@Immutable
data class PlantLocateUiState(
    val mode: PlantLocateMode = PlantLocateMode.Create,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val gardenImageUrl: String = "",
    val existingPlants: LocatedPlantCollection = LocatedPlantCollection(),
    val currentPlant: LocatedCurrentPlantUiModel = LocatedCurrentPlantUiModel(),
    val errorMessage: String? = null,
) {
    val submitButtonText: String
        get() = if (mode == PlantLocateMode.Create) "등록하기" else "저장하기"
}

enum class PlantLocateMode {
    Create,
    Edit,
}

@Immutable
data class LocatedPlantCollection(
    val plants: List<LocatedPlantUiModel> = emptyList(),
)

@Immutable
data class LocatedPlantUiModel(
    val plantId: Int,
    val imageUrl: String,
    val ratioX: Double,
    val ratioY: Double,
    val scale: Int,
)

@Immutable
data class LocatedCurrentPlantUiModel(
    val image: PlantLocateImageUiModel = PlantLocateImageUiModel(),
    val ratioX: Double = 0.42,
    val ratioY: Double = 0.42,
    val scale: Int = -1,
)

@Immutable
data class PlantLocateImageUiModel(
    val value: String = "",
    val type: PlantLocateImageType = PlantLocateImageType.LocalPath,
)

enum class PlantLocateImageType {
    LocalPath,
    RemoteUrl,
}

sealed interface PlantLocateEffect {
    data object SubmitSuccess : PlantLocateEffect
}
