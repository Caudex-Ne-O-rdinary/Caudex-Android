package com.cmc.caudex.presentation.room

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.UploadButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import kotlin.math.roundToInt

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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .clip(RoundedCornerShape(20.dp)),
        ) {
            val containerWidth = constraints.maxWidth.toFloat()
            val containerHeight = constraints.maxHeight.toFloat()
            val density = LocalDensity.current

            if (state.gardenImageUrl.isNotBlank()) {
                AsyncImage(
                    model = state.gardenImageUrl,
                    contentDescription = "정원 배경",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CaudexTheme.colors.k5),
                )
            }

            state.plants.forEach { plant ->
                if (plant.imageUrl.isBlank()) return@forEach

                val plantSize = with(density) { plant.scalePx.toDp() }
                val plantSizePx = plant.scalePx.toFloat()
                val offsetX = (plant.ratioX * containerWidth - plantSizePx / 2).roundToInt()
                val offsetY = (plant.ratioY * containerHeight - plantSizePx / 2).roundToInt()
                AsyncImage(
                    model = plant.imageUrl,
                    contentDescription = "식물",
                    modifier = Modifier
                        .size(plantSize)
                        .offset { IntOffset(offsetX, offsetY) }
                        .clickable { onPlantClick(plant.plantId) },
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

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
