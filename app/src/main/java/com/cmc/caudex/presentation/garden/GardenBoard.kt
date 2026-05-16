package com.cmc.caudex.presentation.garden

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import coil.compose.AsyncImage
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import kotlin.math.roundToInt

@Immutable
data class GardenBoardPlantUiModel(
    val plantId: Int,
    val imageUrl: String,
    val ratioX: Double,
    val ratioY: Double,
    val scalePx: Int,
)

@Immutable
data class GardenBoardMetrics(
    val widthPx: Float,
    val heightPx: Float,
)

@Composable
fun GardenBoard(
    gardenImageUrl: String,
    plants: List<GardenBoardPlantUiModel>,
    modifier: Modifier = Modifier,
    contentModifier: (GardenBoardMetrics) -> Modifier = { Modifier },
    backgroundContentDescription: String = "정원 배경",
    plantContentDescription: String = "식물",
    onPlantClick: ((Int) -> Unit)? = null,
    overlay: @Composable BoxScope.(GardenBoardMetrics) -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {
        val metrics = GardenBoardMetrics(
            widthPx = constraints.maxWidth.toFloat(),
            heightPx = constraints.maxHeight.toFloat(),
        )
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(contentModifier(metrics)),
        ) {
            if (gardenImageUrl.isNotBlank()) {
                AsyncImage(
                    model = gardenImageUrl,
                    contentDescription = backgroundContentDescription,
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

            plants.forEach { plant ->
                val plantSize = with(density) { plant.scalePx.toDp() }
                val plantSizePx = plant.scalePx.toFloat()
                val offsetX = (plant.ratioX * metrics.widthPx - plantSizePx / 2).roundToInt()
                val offsetY = (plant.ratioY * metrics.heightPx - plantSizePx / 2).roundToInt()
                val clickModifier = if (onPlantClick != null) {
                    Modifier.clickable { onPlantClick(plant.plantId) }
                } else {
                    Modifier
                }

                AsyncImage(
                    model = plant.imageUrl,
                    contentDescription = plantContentDescription,
                    modifier = Modifier
                        .size(plantSize)
                        .offset { IntOffset(offsetX, offsetY) }
                        .then(clickModifier),
                    contentScale = ContentScale.Fit,
                )
            }

            overlay(metrics)
        }
    }
}
