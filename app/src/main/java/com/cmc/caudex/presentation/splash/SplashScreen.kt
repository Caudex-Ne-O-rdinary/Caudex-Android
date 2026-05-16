package com.cmc.caudex.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1700L)
        onSplashComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CaudexTheme.colors.k50),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        // TODO: 앱 이름 또는 로고 텍스트로 교체
        Text(
            text = "Caudex",
            color = CaudexTheme.colors.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.offset { IntOffset(x = 0, y = -300) },
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    CaudexTheme {
        SplashScreen { }
    }
}
