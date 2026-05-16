package com.cmc.caudex.presentation.room

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.UploadButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun RoomScreen(
    modifier: Modifier = Modifier,
) {
    RoomContent(modifier = modifier)
}

@Composable
private fun RoomContent(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaudexTheme.colors.k50,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                UploadButton(
                    onClick = { },
                )
                CaudexButton(
                    modifier = Modifier.weight(1f),
                    text = "식물 업로드하기",
                    onClick = { },
                )
            }
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
                    .padding(horizontal = 20.dp, vertical = 40.dp)
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

@Composable
fun Checkerboard(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(CaudexTheme.colors.k5),
    )
}

@Preview(showBackground = true)
@Composable
private fun RoomScreenPreview() {
    CaudexTheme {
        RoomScreen()
    }
}
