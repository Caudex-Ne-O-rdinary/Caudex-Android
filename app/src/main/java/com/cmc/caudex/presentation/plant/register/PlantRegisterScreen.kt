package com.cmc.caudex.presentation.plant.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.components.CaudexAddImg
import com.cmc.caudex.presentation.designsystem.components.CaudexButton
import com.cmc.caudex.presentation.designsystem.components.CaudexTextField
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import com.cmc.caudex.presentation.navigation.PlantLocateRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantRegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLocate: (PlantLocateRoute) -> Unit,
    gardenId: String = "",
    isGuestRoom: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: PlantRegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            val path = it.toString()
            viewModel.updateImage(path)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PlantRegisterEffect.NavigateToLocate -> {
                    onNavigateToLocate(
                        PlantLocateRoute(
                            gardenId = gardenId,
                            isGuestRoom = isGuestRoom,
                            imagePath = effect.imagePath,
                            plantName = effect.plantName,
                            managementTip = effect.managementTip,
                            diary = effect.diary,
                        )
                    )
                }
            }
        }
    }

    PlantRegisterContent(
        imagePath = state.imagePath,
        plantName = state.plantName,
        managementTip = state.managementTip,
        diary = state.diary,
        canProceed = state.canProceed,
        onNavigateBack = onNavigateBack,
        onAddImgClick = { imagePickerLauncher.launch("image/*") },
        onPlantNameChange = viewModel::updatePlantName,
        onManagementTipChange = viewModel::updateManagementTip,
        onDiaryChange = viewModel::updateDiary,
        onNextClick = viewModel::onNextClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantRegisterContent(
    imagePath: String,
    plantName: String,
    managementTip: String,
    diary: String,
    canProceed: Boolean,
    onNavigateBack: () -> Unit,
    onAddImgClick: () -> Unit,
    onPlantNameChange: (String) -> Unit,
    onManagementTipChange: (String) -> Unit,
    onDiaryChange: (String) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

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
                    onClick = onNextClick,
                    enabled = canProceed
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
                if (imagePath.isNotBlank()) {
                    AsyncImage(
                        model = Uri.parse(imagePath),
                        contentDescription = "선택한 식물 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onAddImgClick() },
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    CaudexAddImg(onClick = onAddImgClick)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CaudexTextField(
                title = "식물 이름",
                value = plantName,
                onValueChange = onPlantNameChange,
                hint = "이름을 입력하세요",
                maxLength = 10,
                guideText = "최대 10자까지 입력할 수 있어요"
            )

            Spacer(modifier = Modifier.height(24.dp))

            CaudexTextField(
                title = "관리 TIP",
                value = managementTip,
                onValueChange = onManagementTipChange,
                hint = "내용을 작성하세요",
                maxLength = 100,
                guideText = "최대 100자까지 입력할 수 있어요",
                height = 335.dp,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            CaudexTextField(
                title = "식물 일기",
                value = diary,
                onValueChange = onDiaryChange,
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
        PlantRegisterContent(
            imagePath = "",
            plantName = "",
            managementTip = "",
            diary = "",
            canProceed = false,
            onNavigateBack = {},
            onAddImgClick = {},
            onPlantNameChange = {},
            onManagementTipChange = {},
            onDiaryChange = {},
            onNextClick = {},
        )
    }
}
