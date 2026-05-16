package com.cmc.caudex.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun CaudexTheme(
    content: @Composable () -> Unit,
) {
    val typography = DefaultCaudexTypography
    val colorScheme = LightCaudexColors

    CompositionLocalProvider(
        LocalCaudexTypography provides typography,
        LocalCaudexColors provides colorScheme,
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

object CaudexTheme {
    val typography: CaudexTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalCaudexTypography.current

    val colors: PickleColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCaudexColors.current
}
