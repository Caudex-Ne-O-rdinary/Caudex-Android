package com.cmc.caudex.presentation.room.guest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.cmc.caudex.presentation.util.GARDEN_LINK_CLIP_LABEL
import com.cmc.caudex.presentation.util.LINK_COPIED_MESSAGE
import com.cmc.caudex.presentation.util.copyTextToClipboard

@Composable
fun GuestRoomScreen(
    onNavigateToPlantRegister: () -> Unit,
    onNavigateToPlantFriend: (plantId: Int, isMine: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuestRoomViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshGarden()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GuestRoomEffect.NavigateToPlantFriend -> onNavigateToPlantFriend(effect.plantId, effect.isMine)
                is GuestRoomEffect.CopyLinkToClipboard -> {
                    context.copyTextToClipboard(
                        label = GARDEN_LINK_CLIP_LABEL,
                        text = effect.link,
                        toastMessage = LINK_COPIED_MESSAGE,
                    )
                }
            }
        }
    }

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
                UploadButton(onClick = viewModel::onShareClick)
                CaudexButton(
                    modifier = Modifier.weight(1f),
                    text = "식물 업로드하기",
                    onClick = onNavigateToPlantRegister,
                )
            }
        },
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = CaudexTheme.colors.primary)
                }
            }

            else -> {
                GardenBoard(
                    gardenImageUrl = state.gardenImageUrl,
                    plants = state.plants.map { it.toGardenBoardPlantUiModel() },
                    onPlantClick = viewModel::onPlantClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp, vertical = 40.dp)
                        .clip(RoundedCornerShape(20.dp)),
                )
            }
        }
    }
}

private fun GuestPlantUiModel.toGardenBoardPlantUiModel(): GardenBoardPlantUiModel =
    GardenBoardPlantUiModel(
        plantId = plantId,
        imageUrl = imageUrl,
        ratioX = ratioX,
        ratioY = ratioY,
        scalePx = scalePx,
    )
