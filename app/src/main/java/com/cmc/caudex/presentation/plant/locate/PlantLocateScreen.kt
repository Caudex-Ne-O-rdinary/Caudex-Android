package com.cmc.caudex.presentation.plant.locate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import com.cmc.caudex.presentation.room.Checkerboard

@Composable
fun PlantLocateScreen() {
    PlantLocateScreenContent()
}

@Composable
private fun PlantLocateScreenContent(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaudexTheme.colors.k50,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .background(CaudexTheme.colors.k50),
                contentAlignment = Alignment.CenterStart,
            ) {
                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron),
                        contentDescription = "Back",
                        tint = CaudexTheme.colors.k900,
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "식물 위치 조정",
                    style = CaudexTheme.typography.title1,
                    color = CaudexTheme.colors.k900,
                )
            }
        },
        bottomBar = {
            CaudexButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                text = "등록하기",
                onClick = { },
            )

        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 27.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
            ) {
                Checkerboard(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantLocateScreenPreview() {
    CaudexTheme {
        PlantLocateScreen()
    }
}
