package com.cmc.caudex.presentation.room.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.cmc.caudex.presentation.designsystem.components.CaudexTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun RoomCreateScreen(
    onNavigateToNext: () -> Unit,
    modifier: Modifier = Modifier
) {

    val nicknameState = remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                CaudexButton(
                    text = "방 생성하기",
                    onClick = onNavigateToNext
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {

            CaudexTextField(
                title = "닉네임",
                value = nicknameState.value,
                onValueChange = { nicknameState.value = it },
                hint = "닉네임을 입력해주세요",
                maxLength = 4,
                guideText = "최대 4자까지 입력할 수 있어요"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "배경 선택",
                style = CaudexTheme.typography.body2,
                color = CaudexTheme.colors.k900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(CaudexTheme.colors.k100)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(CaudexTheme.colors.k100)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(CaudexTheme.colors.k100)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "배경 미리보기",
                style = CaudexTheme.typography.body2,
                color = CaudexTheme.colors.k900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(CaudexTheme.colors.k100)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoomCreateScreenPreview() {
    CaudexTheme {
        RoomCreateScreen(onNavigateToNext = {})
    }
}