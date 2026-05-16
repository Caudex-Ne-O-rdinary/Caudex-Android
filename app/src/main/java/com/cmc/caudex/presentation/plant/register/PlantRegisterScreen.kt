package com.cmc.caudex.presentation.plant.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.components.CaudexAddImg
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.CaudexTextField
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantRegisterScreen(
    onNavigateBack: () -> Unit,
    onAddImgClick: () -> Unit,
    onNavigateToNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plantNameState = remember { mutableStateOf("") }
    val manageTipState = remember { mutableStateOf("") }
    val plantDiaryState = remember { mutableStateOf("") }

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
                        text = "식물 업로드",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
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
                    enabled = isButtonEnabled.value
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                CaudexAddImg(
                    onClick = onAddImgClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            CaudexTextField(
                title = "식물 이름",
                value = plantNameState.value,
                onValueChange = { plantNameState.value = it },
                hint = "이름을 입력하세요",
                maxLength = 10,
                guideText = "최대 10자까지 입력할 수 있어요"
            )

            Spacer(modifier = Modifier.height(24.dp))

            CaudexTextField(
                title = "관리 TIP",
                value = manageTipState.value,
                onValueChange = { manageTipState.value = it },
                hint = "내용을 작성하세요",
                maxLength = 100,
                guideText = "최대 100자까지 입력할 수 있어요",
                height = 335.dp,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            CaudexTextField(
                title = "식물 일기",
                value = plantDiaryState.value,
                onValueChange = { plantDiaryState.value = it },
                hint = "내용을 작성하세요",
                maxLength = 100,
                guideText = "최대 100자까지 입력할 수 있어요",
                height = 335.dp,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantRegisterScreenPreview() {
    CaudexTheme {
        PlantRegisterScreen(
            onNavigateBack = {},
            onAddImgClick = {},
            onNavigateToNext = {}
        )
    }
}