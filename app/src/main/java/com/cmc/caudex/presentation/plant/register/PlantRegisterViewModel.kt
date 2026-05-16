package com.cmc.caudex.presentation.plant.register

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlantRegisterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PlantRegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PlantRegisterEffect>()
    val effect = _effect.asSharedFlow()

    fun updateImage(imagePath: String) {
        _uiState.update { it.copy(imagePath = imagePath) }
    }

    fun updatePlantName(name: String) {
        _uiState.update { it.copy(plantName = name.take(MAX_NAME_LENGTH)) }
    }

    fun updateManagementTip(tip: String) {
        _uiState.update { it.copy(managementTip = tip.take(MAX_CONTENT_LENGTH)) }
    }

    fun updateDiary(diary: String) {
        _uiState.update { it.copy(diary = diary.take(MAX_CONTENT_LENGTH)) }
    }

    fun onNextClick() {
        val state = _uiState.value
        if (!state.canProceed) return

        viewModelScope.launch {
            _effect.emit(
                PlantRegisterEffect.NavigateToLocate(
                    imagePath = state.imagePath,
                    plantName = state.plantName,
                    managementTip = state.managementTip,
                    diary = state.diary,
                )
            )
        }
    }

    private companion object {
        const val MAX_NAME_LENGTH = 10
        const val MAX_CONTENT_LENGTH = 100
    }
}

@Immutable
data class PlantRegisterUiState(
    val imagePath: String = "",
    val plantName: String = "",
    val managementTip: String = "",
    val diary: String = "",
) {
    val canProceed: Boolean
        get() = imagePath.isNotBlank() &&
            plantName.isNotBlank() &&
            managementTip.isNotBlank() &&
            diary.isNotBlank()
}

sealed interface PlantRegisterEffect {
    data class NavigateToLocate(
        val imagePath: String,
        val plantName: String,
        val managementTip: String,
        val diary: String,
    ) : PlantRegisterEffect
}
