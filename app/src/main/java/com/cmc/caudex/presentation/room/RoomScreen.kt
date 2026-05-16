package com.cmc.caudex.presentation.room

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.UploadButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import com.cmc.caudex.presentation.garden.GardenBoard
import com.cmc.caudex.presentation.garden.GardenBoardPlantUiModel

@Composable
fun RoomScreen(
    onNavigateToPlantRegister: (gardenId: String) -> Unit,
    onNavigateToPlantFriend: (plantId: Int, isMine: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshGarden()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RoomEffect.CopyLinkToClipboard -> {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("garden_link", effect.link))
                    Toast.makeText(context, "링크가 복사되었어요", Toast.LENGTH_SHORT).show()
                }
                is RoomEffect.NavigateToPlantFriend -> {
                    onNavigateToPlantFriend(effect.plantId, effect.isMine)
                }
            }
        }
    }

    RoomContent(
        state = state,
        onNavigateToPlantRegister = onNavigateToPlantRegister,
        onUploadButtonClick = viewModel::onUploadButtonClick,
        onPlantClick = viewModel::onPlantClick,
        modifier = modifier,
    )
}

@Composable
private fun RoomContent(
    state: RoomUiState,
    onNavigateToPlantRegister: (gardenId: String) -> Unit,
    onUploadButtonClick: () -> Unit,
    onPlantClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaudexTheme.colors.k50,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                UploadButton(onClick = onUploadButtonClick)
                CaudexButton(
                    modifier = Modifier.weight(1f),
                    text = "식물 업로드하기",
                    onClick = { onNavigateToPlantRegister(state.gardenId) },
                    enabled = state.gardenId.isNotBlank(),
                )
            }
        },
    ) { innerPadding ->
        GardenBoard(
            gardenImageUrl = state.gardenImageUrl,
            plants = state.plants
                .filter { it.imageUrl.isNotBlank() }
                .map { it.toGardenBoardPlantUiModel() },
            onPlantClick = onPlantClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .clip(RoundedCornerShape(20.dp)),
        )
    }
}

private fun RoomPlantUiModel.toGardenBoardPlantUiModel(): GardenBoardPlantUiModel =
    GardenBoardPlantUiModel(
        plantId = plantId,
        imageUrl = imageUrl,
        ratioX = ratioX,
        ratioY = ratioY,
        scalePx = scalePx,
    )

@Preview(showBackground = true)
@Composable
private fun RoomScreenPreview() {
    CaudexTheme {
        RoomContent(
            state = RoomUiState(),
            onNavigateToPlantRegister = { _ -> },
            onUploadButtonClick = {},
            onPlantClick = {},
        )
    }
}
