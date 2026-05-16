package com.cmc.caudex.presentation.plant.friend

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cmc.caudex.domain.model.Diary
import com.cmc.caudex.domain.usecase.GetPlantUseCase
import com.cmc.caudex.domain.usecase.WriteDiaryUseCase
import com.cmc.caudex.presentation.navigation.PlantFriendRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlantFriendViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPlantUseCase: GetPlantUseCase,
    private val writeDiaryUseCase: WriteDiaryUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<PlantFriendRoute>()
    private val plantId: Int = route.plantId
    private val isMine: Boolean = route.isMine

    private val _uiState = MutableStateFlow(PlantFriendUiState(isMine = isMine))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PlantFriendEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadPlant()
    }

    fun retryLoad() {
        loadPlant()
    }

    fun onDiaryInputChange(text: String) {
        _uiState.update { it.copy(diaryInput = text) }
    }

    fun onWriteDiary() {
        val input = _uiState.value.diaryInput.trim()
        if (input.isBlank() || _uiState.value.isSubmittingDiary) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingDiary = true) }
            writeDiaryUseCase(plantId, input)
                .onSuccess {
                    _uiState.update { it.copy(diaryInput = "", isSubmittingDiary = false) }
                    loadPlant()
                    _effect.emit(PlantFriendEffect.ShowToast("일기가 등록됐어요"))
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isSubmittingDiary = false) }
                    _effect.emit(PlantFriendEffect.ShowToast(throwable.message ?: "등록에 실패했어요"))
                }
        }
    }

    private fun loadPlant() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getPlantUseCase(plantId)
                .onSuccess { plant ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plantName = plant.name,
                            imageUrl = plant.imageUrl,
                            managementTip = plant.managementTip,
                            diaries = plant.diaries.map { diary -> diary.toUiModel() },
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "식물 정보를 불러오지 못했어요",
                        )
                    }
                }
        }
    }

    private fun Diary.toUiModel(): PlantDiaryUiModel =
        PlantDiaryUiModel(
            diaryId = diaryId,
            content = content,
            createdAt = createdAt,
        )
}

@Immutable
data class PlantFriendUiState(
    val isLoading: Boolean = false,
    val isMine: Boolean = false,
    val plantName: String = "",
    val imageUrl: String = "",
    val managementTip: String = "",
    val diaries: List<PlantDiaryUiModel> = emptyList(),
    val errorMessage: String? = null,
    val diaryInput: String = "",
    val isSubmittingDiary: Boolean = false,
)

sealed interface PlantFriendEffect {
    data class ShowToast(val message: String) : PlantFriendEffect
}

@Immutable
data class PlantDiaryUiModel(
    val diaryId: Int,
    val content: String,
    val createdAt: String,
)
