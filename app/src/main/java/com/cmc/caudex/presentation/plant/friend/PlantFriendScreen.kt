package com.cmc.caudex.presentation.plant.friend

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantFriendScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantFriendViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PlantFriendEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    PlantFriendContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onNavigateToNext = onNavigateToNext,
        onRetry = viewModel::retryLoad,
        onDiaryInputChange = viewModel::onDiaryInputChange,
        onWriteDiary = viewModel::onWriteDiary,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantFriendContent(
    state: PlantFriendUiState,
    onNavigateBack: () -> Unit,
    onNavigateToNext: () -> Unit,
    onRetry: () -> Unit,
    onDiaryInputChange: (String) -> Unit,
    onWriteDiary: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.plantName.ifBlank { "식물 정보" },
                        style = CaudexTheme.typography.title1,
                        color = CaudexTheme.colors.k900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.Unspecified
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            if (state.isMine) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        value = state.diaryInput,
                        onValueChange = onDiaryInputChange,
                        placeholder = {
                            Text(
                                text = "오늘의 식물 일기를 작성해보세요",
                                style = CaudexTheme.typography.body4,
                                color = CaudexTheme.colors.k400,
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = CaudexTheme.colors.k5,
                            unfocusedContainerColor = CaudexTheme.colors.k5,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CaudexButton(
                        text = "등록",
                        onClick = onWriteDiary,
                        enabled = state.diaryInput.isNotBlank() && !state.isSubmittingDiary,
                        modifier = Modifier.width(72.dp),
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .navigationBarsPadding()
                ) {
                    CaudexButton(
                        text = "다음",
                        onClick = onNavigateToNext,
                    )
                }
            }
        }
    ) { innerPadding ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = CaudexTheme.colors.primary,
                    )
                }
            }

            state.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage,
                            style = CaudexTheme.typography.body4,
                            color = CaudexTheme.colors.k600,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        CaudexButton(
                            text = "다시 시도",
                            onClick = onRetry,
                            modifier = Modifier.fillMaxWidth(0.5f),
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CaudexTheme.colors.k5)
                            .padding(top = 19.dp, start = 20.dp, end = 20.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(width = 296.dp, height = 180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CaudexTheme.colors.k100)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            if (state.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = state.imageUrl,
                                    contentDescription = "${state.plantName} 이미지",
                                    modifier = Modifier.matchParentSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = state.plantName,
                            style = CaudexTheme.typography.title1,
                            color = CaudexTheme.colors.k900
                        )

                        if (state.managementTip.isNotBlank()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "관리 TIP",
                                style = CaudexTheme.typography.body2,
                                color = CaudexTheme.colors.k900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = state.managementTip,
                                style = CaudexTheme.typography.body4,
                                color = CaudexTheme.colors.k900
                            )
                        }

                        if (state.diaries.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "식물 일기",
                                style = CaudexTheme.typography.body2,
                                color = CaudexTheme.colors.k900
                            )

                            state.diaries.forEach { diary ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = diary.content,
                                    style = CaudexTheme.typography.body4,
                                    color = CaudexTheme.colors.k900
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(34.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantFriendScreenPreview() {
    CaudexTheme {
        PlantFriendContent(
            state = PlantFriendUiState(
                plantName = "낙네임 식물",
                managementTip = "오늘은 맑은 하늘 아래 바람이 부드럽게 불었다.",
                diaries = listOf(
                    PlantDiaryUiModel(1, "오늘은 맑은 하늘 아래 바람이 부드럽게 불었다.", "2024-01-01"),
                ),
            ),
            onNavigateBack = {},
            onNavigateToNext = {},
            onRetry = {},
            onDiaryInputChange = {},
            onWriteDiary = {},
        )
    }
}
