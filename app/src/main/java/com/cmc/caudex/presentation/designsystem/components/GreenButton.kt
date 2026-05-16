package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmc.caudex.presentation.designsystem.theme.LocalCaudexColors
import com.cmc.caudex.presentation.designsystem.theme.LocalCaudexTypography

@Composable
fun GreenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = LocalCaudexColors.current
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.k5,
            disabledContainerColor = colors.k400,
            disabledContentColor = colors.k5
        )
    ) {
        Text(
            text = text,
            style = LocalCaudexTypography.current.title2
        )
    }
}