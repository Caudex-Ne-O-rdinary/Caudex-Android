package com.cmc.caudex.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cmc.caudex.R
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class CaudexTypography(
    val fontFamily: FontFamily = pretendardFamily,

    // HeadLine
    val head1: TextStyle,

    // Title
    val title1: TextStyle,
    val title2: TextStyle,

    // Body
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,

    //caption
    val caption1: TextStyle,
)
private val pretendardFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.W400),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
)

val DefaultCaudexTypography = CaudexTypography(
    head1 = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.4.sp,
        letterSpacing = -0.12.sp,
        fontWeight = FontWeight.SemiBold
    ),
    title1 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 27.sp,
        letterSpacing = -0.1.sp,
        fontWeight = FontWeight.SemiBold
    ),
    title2 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 21.6.sp,
        letterSpacing = -0.08.sp,
        fontWeight = FontWeight.SemiBold
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = -0.16.sp,
        fontWeight = FontWeight.W400
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = -0.14.sp,
        fontWeight = FontWeight.SemiBold
    ),
    body3 = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = -0.14.sp,
        fontWeight = FontWeight.Medium
    ),
    caption1 = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = -0.12.sp,
        fontWeight = FontWeight.W400
    ),
)

val LocalCaudexTypography = staticCompositionLocalOf { DefaultCaudexTypography }