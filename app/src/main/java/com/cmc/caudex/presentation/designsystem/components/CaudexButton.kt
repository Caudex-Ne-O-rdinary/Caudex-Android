package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import com.cmc.caudex.presentation.designsystem.theme.LocalCaudexTypography

@Composable
fun CaudexButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CaudexTheme.colors.primary,
            contentColor = CaudexTheme.colors.k5,
            disabledContainerColor = CaudexTheme.colors.k400,
            disabledContentColor = CaudexTheme.colors.k5
        )
    ) {
        Text(
            text = text,
            style = LocalCaudexTypography.current.title2
        )
    }
}

@Preview
@Composable
private fun CaudexButtonEnablePreview() {
    CaudexTheme {
        CaudexButton(
            text = "확인",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun CaudexButtonDisablePreview() {
    CaudexTheme {
        CaudexButton(
            text = "확인",
            onClick = {},
            enabled = false
        )
    }
}
