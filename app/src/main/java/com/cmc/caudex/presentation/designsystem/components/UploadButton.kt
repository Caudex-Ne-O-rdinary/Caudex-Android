package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun UploadButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(54.dp)
            .background(
                color = CaudexTheme.colors.k900,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_link),
            contentDescription = "Link",
            tint = CaudexTheme.colors.k5
        )
    }
}

@Preview
@Composable
private fun UploadButtonPreview() {
    CaudexTheme {
        UploadButton(
            onClick = {},
        )
    }
}