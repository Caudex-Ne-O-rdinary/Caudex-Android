package com.cmc.caudex.presentation.plant.locate

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import kotlin.math.roundToInt

private const val PLANT_SCALE_MIN_DP = 40
private const val PLANT_SCALE_MAX_DP = 200
private const val PLANT_SCALE_DEFAULT_DP = 80

private fun Offset.isInsidePlantBounds(
    ratioX: Double,
    ratioY: Double,
    containerWidth: Float,
    containerHeight: Float,
    scalePx: Int,
): Boolean {
    val halfSize = scalePx / 2f
    val centerX = (ratioX * containerWidth).toFloat()
    val centerY = (ratioY * containerHeight).toFloat()
    return x in (centerX - halfSize)..(centerX + halfSize) &&
        y in (centerY - halfSize)..(centerY + halfSize)
}

@Composable
fun PlantLocateScreen(
    onNavigateBack: () -> Unit,
    onSubmitSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantLocateViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PlantLocateEffect.SubmitSuccess -> onSubmitSuccess()
                is PlantLocateEffect.ShowToast -> Toast.makeText(
                    context,
                    effect.message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    PlantLocateScreenContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onSubmit = viewModel::submit,
        onUpdatePosition = viewModel::updatePlantPosition,
        onUpdateScale = viewModel::updatePlantScale,
        modifier = modifier,
    )
}

@Composable
private fun PlantLocateScreenContent(
    state: PlantLocateUiState,
    onNavigateBack: () -> Unit,
    onSubmit: (Int) -> Unit,
    onUpdatePosition: (Double, Double) -> Unit,
    onUpdateScale: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val minScalePx = with(density) { PLANT_SCALE_MIN_DP.dp.toPx() }.roundToInt()
    val maxScalePx = with(density) { PLANT_SCALE_MAX_DP.dp.toPx() }.roundToInt()
    val defaultScalePx = with(density) { PLANT_SCALE_DEFAULT_DP.dp.toPx() }.roundToInt()
    val currentScalePx = if (state.currentPlant.scalePx <= 0) {
        defaultScalePx
    } else {
        state.currentPlant.scalePx
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaudexTheme.colors.k50,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .background(CaudexTheme.colors.k50),
                contentAlignment = Alignment.CenterStart,
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron),
                        contentDescription = "Back",
                        tint = CaudexTheme.colors.k900,
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "식물 위치 조정",
                    style = CaudexTheme.typography.title1,
                    color = CaudexTheme.colors.k900,
                )
            }
        },
        bottomBar = {
            CaudexButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
                text = state.submitButtonText,
                onClick = { onSubmit(currentScalePx) },
                enabled = !state.isSaving,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 12.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp)),
            ) {
                val w = constraints.maxWidth.toFloat()
                val h = constraints.maxHeight.toFloat()

                // rememberUpdatedState로 pointerInput 람다 내부에서 항상 최신값 사용
                val latestRatioX by rememberUpdatedState(state.currentPlant.ratioX)
                val latestRatioY by rememberUpdatedState(state.currentPlant.ratioY)
                val latestScalePx by rememberUpdatedState(currentScalePx)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(w, h, minScalePx, maxScalePx, state.currentPlant.image.value) {
                            awaitEachGesture {
                                if (state.currentPlant.image.value.isBlank()) return@awaitEachGesture

                                val firstDown = awaitFirstDown(requireUnconsumed = false)
                                var ratioX = latestRatioX
                                var ratioY = latestRatioY
                                var scalePx = latestScalePx
                                if (!firstDown.position.isInsidePlantBounds(ratioX, ratioY, w, h, scalePx)) {
                                    return@awaitEachGesture
                                }

                                var anchorPointerId = firstDown.id
                                var secondPointerId: PointerId? = null
                                var initialDistancePx = 0f
                                var initialScalePx = scalePx
                                var previousAnchorPosition = firstDown.position
                                firstDown.consume()

                                while (true) {
                                    val event = awaitPointerEvent()
                                    val pressedChanges = event.changes.filter { it.pressed }
                                    if (pressedChanges.isEmpty()) break

                                    val anchorChange = pressedChanges.firstOrNull { it.id == anchorPointerId }
                                        ?: pressedChanges.first().also { anchorPointerId = it.id }
                                    val secondChange = secondPointerId
                                        ?.let { pointerId -> pressedChanges.firstOrNull { it.id == pointerId } }
                                        ?: pressedChanges.firstOrNull { it.id != anchorPointerId }

                                    if (secondChange != null) {
                                        val distancePx = (secondChange.position - anchorChange.position).getDistance()
                                        if (secondPointerId != secondChange.id || initialDistancePx <= 0f) {
                                            secondPointerId = secondChange.id
                                            initialDistancePx = distancePx
                                            initialScalePx = scalePx
                                        }

                                        if (initialDistancePx > 0f) {
                                            scalePx = (initialScalePx * distancePx / initialDistancePx)
                                                .roundToInt()
                                                .coerceIn(minScalePx, maxScalePx)
                                            onUpdateScale(scalePx)
                                        }

                                        previousAnchorPosition = anchorChange.position
                                        event.changes.forEach { it.consume() }
                                    } else {
                                        secondPointerId = null
                                        initialDistancePx = 0f
                                        val delta = anchorChange.position - previousAnchorPosition
                                        previousAnchorPosition = anchorChange.position
                                        ratioX = (ratioX + delta.x / w).coerceIn(0.0, 1.0)
                                        ratioY = (ratioY + delta.y / h).coerceIn(0.0, 1.0)
                                        onUpdatePosition(ratioX, ratioY)
                                        anchorChange.consume()
                                    }
                                }
                            }
                        },
                ) {
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

                    state.existingPlants.plants.forEach { plant ->
                        val sizeDp = with(density) { plant.scalePx.toDp() }
                        val sizePx = plant.scalePx.toFloat()
                        AsyncImage(
                            model = plant.imageUrl,
                            contentDescription = "식물",
                            modifier = Modifier
                                .size(sizeDp)
                                .offset {
                                    IntOffset(
                                        (plant.ratioX * w - sizePx / 2).roundToInt(),
                                        (plant.ratioY * h - sizePx / 2).roundToInt(),
                                    )
                                },
                            contentScale = ContentScale.Fit,
                        )
                    }

                    if (state.currentPlant.image.value.isNotBlank()) {
                        val plantSizeDp = with(density) { currentScalePx.toDp() }
                        val plantSizePx = currentScalePx.toFloat()
                        val imageModel = if (state.currentPlant.image.type == PlantLocateImageType.LocalPath) {
                            Uri.parse(state.currentPlant.image.value)
                        } else {
                            state.currentPlant.image.value
                        }

                        AsyncImage(
                            model = imageModel,
                            contentDescription = "내 식물",
                            modifier = Modifier
                                .size(plantSizeDp)
                                .offset {
                                    IntOffset(
                                        (latestRatioX * w - plantSizePx / 2).roundToInt(),
                                        (latestRatioY * h - plantSizePx / 2).roundToInt(),
                                    )
                                },
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }

            state.errorMessage?.let { message ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    text = message,
                    style = CaudexTheme.typography.body4,
                    color = CaudexTheme.colors.k600,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantLocateScreenPreview() {
    CaudexTheme {
        PlantLocateScreenContent(
            state = PlantLocateUiState(),
            onNavigateBack = {},
            onSubmit = { _ -> },
            onUpdatePosition = { _, _ -> },
            onUpdateScale = {},
        )
    }
}
