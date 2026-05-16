package com.cmc.caudex.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun SplashScreen(
    onNavigateToRoomCreate: () -> Unit,
    onNavigateToRoom: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.destination.collect { destination ->
            when (destination) {
                SplashViewModel.Destination.RoomCreate -> onNavigateToRoomCreate()
                SplashViewModel.Destination.Room -> onNavigateToRoom()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CaudexTheme.colors.k5),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.oddnary),
            contentDescription = "ODDNARY LOGO",
            modifier = Modifier
                .size(width = 180.dp, height = 40.dp)
                .offset { IntOffset(x = 0, y = -300) },
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    CaudexTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CaudexTheme.colors.k5),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.oddnary),
                contentDescription = "ODDNARY LOGO",
                modifier = Modifier
                    .size(width = 180.dp, height = 40.dp)
                    .offset { IntOffset(x = 0, y = -300) },
            )
        }
    }
}
