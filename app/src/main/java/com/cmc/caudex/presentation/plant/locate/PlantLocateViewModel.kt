package com.cmc.caudex.presentation.plant.locate

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cmc.caudex.data.local.GardenPreferencesDataSource
import com.cmc.caudex.domain.model.Plant
import com.cmc.caudex.domain.usecase.GetGardenUseCase
import com.cmc.caudex.domain.usecase.UpdatePlantPositionUseCase
import com.cmc.caudex.domain.usecase.UploadPlantUseCase
import com.cmc.caudex.domain.usecase.WriteDiaryUseCase
import com.cmc.caudex.presentation.navigation.PlantLocateRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    savedStateHandle: SavedStateHandle,
    private val getGardenUseCase: GetGardenUseCase,
    private val uploadPlantUseCase: UploadPlantUseCase,
    private val updatePlantPositionUseCase: UpdatePlantPositionUseCase,
    private val writeDiaryUseCase: WriteDiaryUseCase,
    private val gardenPreferencesDataSource: GardenPreferencesDataSource,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<PlantLocateRoute>()
    private val routeGardenId: String = route.gardenId
    private val imagePath: String = route.imagePath
    private val plantName: String = route.plantName
    private val managementTip: String = route.managementTip
    private val diary: String = route.diary

    private val mode = PlantLocateMode.Create

    private val _uiState = MutableStateFlow(
        PlantLocateUiState(
            mode = mode,
            currentPlant = LocatedCurrentPlantUiModel(
                image = PlantLocateImageUiModel(
                    value = imagePath,
                    type = PlantLocateImageType.LocalPath,
                ),
                ratioX = DEFAULT_RATIO_X,
                ratioY = DEFAULT_RATIO_Y,
                scalePx = DEFAULT_SCALE_PX,
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

    fun updatePlantScale(scalePx: Int) {
        _uiState.update {
            it.copy(
                currentPlant = it.currentPlant.copy(
                    scalePx = scalePx.validScaleOr(DEFAULT_SCALE_PX),
                ),
            )
        }
    }

    fun submit(currentScalePx: Int) {
        val state = _uiState.value
        if (state.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            val currentPlant = _uiState.value.currentPlant.copy(
                scalePx = currentScalePx.validScaleOr(DEFAULT_SCALE_PX_FALLBACK),
            )
            val result = when (mode) {
                PlantLocateMode.Create -> uploadCurrentPlant(currentPlant)
                PlantLocateMode.Edit -> updateCurrentPlantPosition(currentPlant)
            }

            result
                .onSuccess { warningMessage ->
                    _uiState.update { it.copy(isSaving = false) }
                    if (warningMessage != null) {
                        _effect.emit(PlantLocateEffect.ShowToast(warningMessage))
                    }
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

            val gardenId = getTargetGardenId()
            if (gardenId.isBlank()) {
                val templateUrl = gardenPreferencesDataSource.getTemplateUrlOnce()
                _uiState.update { it.copy(isLoading = false, gardenImageUrl = templateUrl) }
                return@launch
            }

            getGardenUseCase(gardenId)
                .onSuccess { garden ->
                    val existingPlants = garden.plants.map { it.toUiModel() }
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            gardenImageUrl = garden.templateUrl,
                            existingPlants = LocatedPlantCollection(existingPlants),
                        )
                    }
                }
                .onFailure {
                    val templateUrl = gardenPreferencesDataSource.getTemplateUrlOnce()
                    _uiState.update {
                        it.copy(isLoading = false, gardenImageUrl = templateUrl)
                    }
                }
        }
    }

    private suspend fun uploadCurrentPlant(currentPlant: LocatedCurrentPlantUiModel): Result<String?> {
        if (imagePath.isBlank()) {
            return Result.failure(IllegalStateException("등록할 식물 이미지가 없어요"))
        }

        val imageFile = runCatching { contentUriToFile(imagePath) }
            .getOrElse { return Result.failure(it) }

        val gardenId = getTargetGardenId()
        if (gardenId.isBlank()) {
            return Result.failure(IllegalStateException("식물을 등록할 정원 정보가 없어요"))
        }

        val plantId = uploadPlantUseCase(
            gardenId = gardenId,
            imageFile = imageFile,
            name = plantName,
            managementTip = managementTip,
            ratioX = currentPlant.ratioX,
            ratioY = currentPlant.ratioY,
            scale = currentPlant.scalePx.takeIf { it > 0 } ?: DEFAULT_SCALE_PX_FALLBACK,
        ).getOrElse { return Result.failure(it) }

        gardenPreferencesDataSource.saveUploadedPlantId(plantId)

        val diaryContent = diary.trim()
        if (diaryContent.isNotBlank()) {
            writeDiaryUseCase(plantId, diaryContent)
                .getOrElse {
                    return Result.success(DIARY_SAVE_FAILED_MESSAGE)
                }
        }

        return Result.success(null)
    }

    private fun contentUriToFile(uriString: String): File {
        val uri = Uri.parse(uriString)
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: error("이미지를 열 수 없어요")
        val temp = File(context.cacheDir, "plant_upload_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            temp.outputStream().use { output -> input.copyTo(output) }
        }
        return temp
    }

    private suspend fun updateCurrentPlantPosition(
        currentPlant: LocatedCurrentPlantUiModel,
    ): Result<String?> {
        val gardenId = getTargetGardenId()
        return updatePlantPositionUseCase(
            gardenId = gardenId,
            plantId = DEFAULT_PLANT_ID,
            ratioX = currentPlant.ratioX,
            ratioY = currentPlant.ratioY,
            scale = currentPlant.scalePx.takeIf { it > 0 } ?: DEFAULT_SCALE_PX_FALLBACK,
        ).map { null }
    }

    private fun Plant.toUiModel(): LocatedPlantUiModel =
        LocatedPlantUiModel(
            plantId = plantId,
            imageUrl = plantUrl,
            ratioX = ratioX.coerceIn(0.0, 1.0),
            ratioY = ratioY.coerceIn(0.0, 1.0),
            scalePx = scale.takeIf { it > 0 } ?: DEFAULT_SCALE_PX_FALLBACK,
        )

    private fun Int.validScaleOr(default: Int): Int =
        takeIf { it > 0 } ?: default

    private suspend fun getTargetGardenId(): String =
        routeGardenId.ifBlank { gardenPreferencesDataSource.getGardenIdOnce() }

    private companion object {
        const val DEFAULT_PLANT_ID = -1
        const val DEFAULT_RATIO_X = 0.42
        const val DEFAULT_RATIO_Y = 0.42
        const val DEFAULT_SCALE_PX = -1
        const val DEFAULT_SCALE_PX_FALLBACK = 80
        const val DIARY_SAVE_FAILED_MESSAGE = "식물은 등록됐지만 일기 저장에 실패했어요"
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
    val scalePx: Int,
)

@Immutable
data class LocatedCurrentPlantUiModel(
    val image: PlantLocateImageUiModel = PlantLocateImageUiModel(),
    val ratioX: Double = 0.42,
    val ratioY: Double = 0.42,
    val scalePx: Int = -1,
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
    data class ShowToast(val message: String) : PlantLocateEffect
}
