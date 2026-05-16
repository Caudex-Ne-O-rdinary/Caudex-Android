package com.cmc.caudex.presentation.room.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.CaudexTextField
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun RoomCreateScreen(
    onNavigateToNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoomCreateViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RoomCreateEffect.NavigateToRoom -> onNavigateToNext()
            }
        }
    }

    LaunchedEffect(state.submitErrorMessage) {
        state.submitErrorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    RoomCreateContent(
        nickname = state.nickname,
        templates = state.templates,
        selectedTemplateId = state.selectedTemplateId,
        selectedTemplateImageUrl = state.selectedTemplate?.imageUrl.orEmpty(),
        isLoadingTemplates = state.isLoadingTemplates,
        isSubmitting = state.isSubmitting,
        templatesErrorMessage = state.templatesErrorMessage,
        canSubmit = state.canSubmit,
        onNicknameChange = viewModel::updateNickname,
        onTemplateSelect = viewModel::selectTemplate,
        onRetryTemplates = viewModel::retryLoadGardenTemplates,
        onSubmit = viewModel::submit,
        modifier = modifier,
    )
}

@Composable
private fun RoomCreateContent(
    nickname: String,
    templates: RoomGardenTemplateCollection,
    selectedTemplateId: Int?,
    selectedTemplateImageUrl: String,
    isLoadingTemplates: Boolean,
    isSubmitting: Boolean,
    templatesErrorMessage: String?,
    canSubmit: Boolean,
    onNicknameChange: (String) -> Unit,
    onTemplateSelect: (Int) -> Unit,
    onRetryTemplates: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaudexTheme.colors.k50,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
            ) {
                CaudexButton(
                    text = "방 생성하기",
                    onClick = onSubmit,
                    enabled = canSubmit,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            CaudexTextField(
                title = "닉네임",
                value = nickname,
                onValueChange = onNicknameChange,
                hint = "닉네임을 입력해주세요",
                maxLength = 4,
                guideText = "최대 4자까지 입력할 수 있어요",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "배경 선택",
                style = CaudexTheme.typography.body2,
                color = CaudexTheme.colors.k900,
            )

            Spacer(modifier = Modifier.height(8.dp))

            GardenTemplateSection(
                templates = templates,
                selectedTemplateId = selectedTemplateId,
                isLoadingTemplates = isLoadingTemplates,
                templatesErrorMessage = templatesErrorMessage,
                onTemplateSelect = onTemplateSelect,
                onRetryTemplates = onRetryTemplates,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "배경 미리보기",
                style = CaudexTheme.typography.body2,
                color = CaudexTheme.colors.k900,
            )

            Spacer(modifier = Modifier.height(8.dp))

            GardenTemplatePreview(
                imageUrl = selectedTemplateImageUrl,
                isLoading = isLoadingTemplates && selectedTemplateImageUrl.isBlank(),
            )
        }
    }
}

@Composable
private fun GardenTemplateSection(
    templates: RoomGardenTemplateCollection,
    selectedTemplateId: Int?,
    isLoadingTemplates: Boolean,
    templatesErrorMessage: String?,
    onTemplateSelect: (Int) -> Unit,
    onRetryTemplates: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        isLoadingTemplates && templates.items.isEmpty() -> {
            LoadingTemplateContent(modifier = modifier)
        }

        templatesErrorMessage != null && templates.items.isEmpty() -> {
            TemplateMessageContent(
                message = templatesErrorMessage,
                actionText = "다시 불러오기",
                onAction = onRetryTemplates,
                modifier = modifier,
            )
        }

        templates.items.isEmpty() -> {
            TemplateMessageContent(
                message = "선택할 수 있는 배경이 없어요",
                modifier = modifier,
            )
        }

        else -> {
            LazyRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = templates.items,
                    key = { it.templateId },
                ) { template ->
                    GardenTemplateItem(
                        template = template,
                        selected = template.templateId == selectedTemplateId,
                        onClick = onTemplateSelect,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingTemplateContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = CaudexTheme.colors.primary,
            strokeWidth = 3.dp,
        )
    }
}

@Composable
private fun TemplateMessageContent(
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = CaudexTheme.typography.body4,
            color = CaudexTheme.colors.k600,
        )

        if (actionText != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = actionText,
                    style = CaudexTheme.typography.body2,
                    color = CaudexTheme.colors.primary,
                )
            }
        }
    }
}

@Composable
private fun GardenTemplateItem(
    template: RoomGardenTemplateUiModel,
    selected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    val borderColor = if (selected) CaudexTheme.colors.primary else CaudexTheme.colors.k200
    val borderWidth = if (selected) 2.dp else 1.dp

    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(shape)
                .background(CaudexTheme.colors.k100)
                .clickable { onClick(template.templateId) },
        ) {
            AsyncImage(
                model = template.imageUrl,
                contentDescription = "${template.name} 배경",
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(borderWidth, borderColor, shape),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = template.name,
            modifier = Modifier.fillMaxWidth(),
            style = CaudexTheme.typography.caption1,
            color = CaudexTheme.colors.k600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun GardenTemplatePreview(
    imageUrl: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(shape)
            .background(CaudexTheme.colors.k100),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = CaudexTheme.colors.primary,
                    strokeWidth = 3.dp,
                )
            }

            imageUrl.isBlank() -> {
                Text(
                    text = "배경을 선택해주세요",
                    style = CaudexTheme.typography.body4,
                    color = CaudexTheme.colors.k600,
                )
            }

            else -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "선택한 배경 미리보기",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Preview(name = "Room Create - Success", showBackground = true)
@Composable
private fun RoomCreateContentPreview() {
    CaudexTheme {
        RoomCreateContent(
            nickname = "까우",
            templates = previewTemplates,
            selectedTemplateId = 1,
            selectedTemplateImageUrl = previewTemplates.items.first().imageUrl,
            isLoadingTemplates = false,
            isSubmitting = false,
            templatesErrorMessage = null,
            canSubmit = true,

            onNicknameChange = {},
            onTemplateSelect = {},
            onRetryTemplates = {},
            onSubmit = {},
        )
    }
}

@Preview(name = "Room Create - Loading", showBackground = true)
@Composable
private fun RoomCreateLoadingPreview() {
    CaudexTheme {
        RoomCreateContent(
            nickname = "",
            templates = RoomGardenTemplateCollection(),
            selectedTemplateId = null,
            selectedTemplateImageUrl = "",
            isLoadingTemplates = true,
            isSubmitting = false,
            templatesErrorMessage = null,
            canSubmit = false,

            onNicknameChange = {},
            onTemplateSelect = {},
            onRetryTemplates = {},
            onSubmit = {},
        )
    }
}

@Preview(name = "Room Create - Error", showBackground = true)
@Composable
private fun RoomCreateErrorPreview() {
    CaudexTheme {
        RoomCreateContent(
            nickname = "까우",
            templates = RoomGardenTemplateCollection(),
            selectedTemplateId = null,
            selectedTemplateImageUrl = "",
            isLoadingTemplates = false,
            isSubmitting = false,
            templatesErrorMessage = "배경을 불러오지 못했어요",
            canSubmit = false,

            onNicknameChange = {},
            onTemplateSelect = {},
            onRetryTemplates = {},
            onSubmit = {},
        )
    }
}

private val previewTemplates = RoomGardenTemplateCollection(
    items = listOf(
        RoomGardenTemplateUiModel(
            templateId = 1,
            name = "숲",
            imageUrl = PREVIEW_TEMPLATE_FOREST_URL,
        ),
        RoomGardenTemplateUiModel(
            templateId = 2,
            name = "온실",
            imageUrl = PREVIEW_TEMPLATE_GREENHOUSE_URL,
        ),
        RoomGardenTemplateUiModel(
            templateId = 3,
            name = "창가",
            imageUrl = PREVIEW_TEMPLATE_WINDOW_URL,
        ),
    ),
)

private const val PREVIEW_TEMPLATE_FOREST_URL = "https://caudex.duckdns.org/images/garden-template-1.png"
private const val PREVIEW_TEMPLATE_GREENHOUSE_URL = "https://caudex.duckdns.org/images/garden-template-2.png"
private const val PREVIEW_TEMPLATE_WINDOW_URL = "https://caudex.duckdns.org/images/garden-template-3.png"
