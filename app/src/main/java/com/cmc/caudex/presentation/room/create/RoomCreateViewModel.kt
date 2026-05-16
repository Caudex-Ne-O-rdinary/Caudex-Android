package com.cmc.caudex.presentation.room.create

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.caudex.data.local.GardenPreferencesDataSource
import com.cmc.caudex.domain.model.GardenTemplate
import com.cmc.caudex.domain.usecase.CreateGardenUseCase
import com.cmc.caudex.domain.usecase.GetGardenTemplatesUseCase
import com.cmc.caudex.presentation.navigation.GardenInviteLink
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RoomCreateViewModel @Inject constructor(
    private val getGardenTemplatesUseCase: GetGardenTemplatesUseCase,
    private val createGardenUseCase: CreateGardenUseCase,
    private val gardenPreferencesDataSource: GardenPreferencesDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomCreateUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RoomCreateEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadGardenTemplates()
    }

    fun updateNickname(nickname: String) {
        _uiState.update {
            it.copy(nickname = nickname.take(MAX_NICKNAME_LENGTH))
        }
    }

    fun selectTemplate(templateId: Int) {
        _uiState.update { state ->
            val nextTemplateId = templateId.takeIf { id ->
                state.templates.items.any { it.templateId == id }
            }

            state.copy(selectedTemplateId = nextTemplateId ?: state.selectedTemplateId)
        }
    }

    fun retryLoadGardenTemplates() {
        loadGardenTemplates()
    }

    fun submit() {
        val state = _uiState.value
        if (!state.canSubmit || state.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }

            val templateId = state.selectedTemplateId ?: return@launch
            createGardenUseCase(state.nickname.trim(), templateId)
                .onSuccess { garden ->
                    val gardenId = garden.gardenId.ifBlank {
                        GardenInviteLink.extractGardenId(garden.gardenUrl)
                    }
                    if (gardenId.isBlank()) {
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                submitErrorMessage = "정원 정보를 저장하지 못했어요",
                            )
                        }
                        return@onSuccess
                    }

                    val inviteLink = GardenInviteLink.fromSavedLinkOrId(garden.gardenUrl, gardenId)
                    gardenPreferencesDataSource.saveGarden(gardenId, inviteLink, garden.templateUrl)
                    _uiState.update { it.copy(isSubmitting = false) }
                    _effect.emit(RoomCreateEffect.NavigateToRoom)
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            submitErrorMessage = throwable.message ?: "방 생성에 실패했어요",
                        )
                    }
                }
        }
    }

    private fun loadGardenTemplates() {
        if (_uiState.value.isLoadingTemplates) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingTemplates = true,
                    templatesErrorMessage = null,
                )
            }

            getGardenTemplatesUseCase()
                .onSuccess { templates ->
                    val templateItems = templates.map { it.toUiModel() }

                    _uiState.update { state ->
                        val selectedTemplateId = state.selectedTemplateId
                            ?.takeIf { id -> templateItems.any { it.templateId == id } }
                            ?: templateItems.firstOrNull()?.templateId

                        state.copy(
                            isLoadingTemplates = false,
                            templates = RoomGardenTemplateCollection(templateItems),
                            selectedTemplateId = selectedTemplateId,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoadingTemplates = false,
                            templatesErrorMessage = throwable.message ?: "배경을 불러오지 못했어요",
                        )
                    }
                }
        }
    }

    private fun GardenTemplate.toUiModel(): RoomGardenTemplateUiModel =
        RoomGardenTemplateUiModel(
            templateId = templateId,
            name = name,
            imageUrl = imageUrl,
        )

    private companion object {
        const val MAX_NICKNAME_LENGTH = 4
    }
}

sealed interface RoomCreateEffect {
    data object NavigateToRoom : RoomCreateEffect
}

@Immutable
data class RoomCreateUiState(
    val nickname: String = "",
    val templates: RoomGardenTemplateCollection = RoomGardenTemplateCollection(),
    val selectedTemplateId: Int? = null,
    val isLoadingTemplates: Boolean = false,
    val isSubmitting: Boolean = false,
    val templatesErrorMessage: String? = null,
    val submitErrorMessage: String? = null,
) {
    val selectedTemplate: RoomGardenTemplateUiModel?
        get() = templates.items.firstOrNull { it.templateId == selectedTemplateId }

    val canSubmit: Boolean
        get() = nickname.trim().isNotEmpty() &&
            selectedTemplateId != null &&
            !isLoadingTemplates &&
            !isSubmitting
}


@Immutable
data class RoomGardenTemplateCollection(
    val items: List<RoomGardenTemplateUiModel> = emptyList(),
)

@Immutable
data class RoomGardenTemplateUiModel(
    val templateId: Int,
    val name: String,
    val imageUrl: String,
)
