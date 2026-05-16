package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun CaudexNicknameDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nickname by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CaudexTheme.colors.k5)
                .padding(20.dp)
        ) {
            CaudexTextField(
                title = "닉네임",
                value = nickname,
                onValueChange = { nickname = it },
                hint = "닉네임을 입력하세요",
                maxLength = 4,
                guideText = "최대 4자까지 입력할 수 있어요",
                textFiledColor = CaudexTheme.colors.k50
            )

            Spacer(modifier = Modifier.height(24.dp))

            CaudexButton(
                text = "확인",
                onClick = { onConfirm(nickname) },
                enabled = nickname.isNotEmpty()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CaudexNicknameDialogPreview() {
    CaudexTheme {
        Box(modifier = Modifier.padding(20.dp)) {
            CaudexNicknameDialog(
                onDismissRequest = {},
                onConfirm = {}
            )
        }
    }
}

