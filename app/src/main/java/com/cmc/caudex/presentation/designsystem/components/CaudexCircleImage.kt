package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun CaudexCircleImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopStart,
        modifier = modifier
            .size(60.dp)
            .clip(CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
private fun CaudexCircleImagePreview() {
    CaudexTheme {
        CaudexCircleImage(
            painter = painterResource(id = R.drawable.test),
            contentDescription = "Preview Plant"
        )
    }
}