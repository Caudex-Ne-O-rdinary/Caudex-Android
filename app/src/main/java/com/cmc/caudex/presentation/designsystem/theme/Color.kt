package com.cmc.caudex.presentation.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
object ColorPalette {

    // Primary
    val p50 = Color(0xFFEAF3EE)
    val p200 = Color(0xFFB7CEC1)
    val primary = Color(0xFF0A3A25)
    val p600 = Color(0xFF072D1C)
    val p900 = Color(0xFF041B11)

    // Gray
    val k5 = Color(0xFFFEFEFE)
    val k50 = Color(0xFFF8F7F1)
    val k100 = Color(0xFFF0EEE6)
    val k200 = Color(0xFFD9D5CA)
    val k400 = Color(0xFFA29D90)
    val k600 = Color(0xFF6F6A5F)
    val k900 = Color(0xFF2E2B25)

    // Action
    val red = Color(0xFF03D46F)
    val green = Color(0xFF03D46F)
}

@Immutable
data class PickleColors(
    val p50: Color,
    val p200: Color,
    val primary: Color,
    val p600: Color,
    val p900: Color,
    val k5: Color,
    val k50: Color,
    val k100: Color,
    val k200: Color,
    val k400: Color,
    val k600: Color,
    val k900: Color,
    val red: Color,
    val green: Color
)

val LightCaudexColors = PickleColors(
    p50 = ColorPalette.p50,
    p200 = ColorPalette.p200,
    primary = ColorPalette.primary,
    p600 = ColorPalette.p600,
    p900 = ColorPalette.p900,
    k5 = ColorPalette.k5,
    k50 = ColorPalette.k50,
    k100 = ColorPalette.k100,
    k200 = ColorPalette.k200,
    k400 = ColorPalette.k400,
    k600 = ColorPalette.k600,
    k900 = ColorPalette.k900,
    red = ColorPalette.red,
    green = ColorPalette.green,
)

val LocalCaudexColors = staticCompositionLocalOf { LightCaudexColors }
