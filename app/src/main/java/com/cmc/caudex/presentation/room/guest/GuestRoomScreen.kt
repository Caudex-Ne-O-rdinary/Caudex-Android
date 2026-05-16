package com.cmc.caudex.presentation.room.guest

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.UploadButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import kotlin.math.roundToInt

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
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("garden_link", effect.link))
                    Toast.makeText(context, "링크가 복사되었어요", Toast.LENGTH_SHORT).show()
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
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp, vertical = 40.dp)
                        .clip(RoundedCornerShape(20.dp)),
                ) {
                    val w = constraints.maxWidth.toFloat()
                    val h = constraints.maxHeight.toFloat()
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
                        val plantSize = with(density) { plant.scalePx.toDp() }
                        val plantSizePx = plant.scalePx.toFloat()
                        val offsetX = (plant.ratioX * w - plantSizePx / 2).roundToInt()
                        val offsetY = (plant.ratioY * h - plantSizePx / 2).roundToInt()
                        AsyncImage(
                            model = plant.imageUrl,
                            contentDescription = "식물",
                            modifier = Modifier
                                .size(plantSize)
                                .offset { IntOffset(offsetX, offsetY) }
                                .clickable { viewModel.onPlantClick(plant.plantId) },
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
        }
    }
}
