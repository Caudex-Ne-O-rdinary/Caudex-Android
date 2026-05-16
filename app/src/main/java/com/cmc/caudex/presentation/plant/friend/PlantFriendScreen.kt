package com.cmc.caudex.presentation.plant.friend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantFriendScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plantNameState = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val isButtonEnabled = remember {
        derivedStateOf { plantNameState.value.isNotBlank() }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "닉네임 식물",
                        style = CaudexTheme.typography.title1,
                        color = CaudexTheme.colors.k900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.Unspecified
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                CaudexButton(
                    text = "다음",
                    onClick = onNavigateToNext,
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CaudexTheme.colors.k5)
                    .padding(top = 19.dp, start = 20.dp, end = 20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(width = 296.dp, height = 180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CaudexTheme.colors.k100)
                        .align(Alignment.CenterHorizontally)
                ) {
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "식물이름",
                    style = CaudexTheme.typography.title1,
                    color = CaudexTheme.colors.k900
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "관리 TIP",
                    style = CaudexTheme.typography.body2,
                    color = CaudexTheme.colors.k900
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "내용내용내용내용",
                    style = CaudexTheme.typography.body4,
                    color = CaudexTheme.colors.k900
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "식물 일기",
                    style = CaudexTheme.typography.body2,
                    color = CaudexTheme.colors.k900
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "내용내용내용내용",
                    style = CaudexTheme.typography.body4,
                    color = CaudexTheme.colors.k900
                )

                Spacer(modifier = Modifier.height(34.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantFriendScreenPreview() {
    CaudexTheme {
        PlantFriendScreen(
            onNavigateBack = {},
            onNavigateToNext = {}
        )
    }
}