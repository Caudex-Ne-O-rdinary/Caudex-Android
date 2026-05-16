package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun CaudexTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    maxLength: Int,
    guideText: String,
    height: Dp = 56.dp,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = title,
            style = CaudexTheme.typography.body2,
            color = CaudexTheme.colors.k900
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            textStyle = CaudexTheme.typography.body1.copy(color = CaudexTheme.colors.k900),
            placeholder = {
                Text(
                    text = hint,
                    style = CaudexTheme.typography.body1,
                    color = CaudexTheme.colors.k400
                )
            },
            trailingIcon = {
                if (singleLine && value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Clear text",
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 3,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CaudexTheme.colors.k5,
                unfocusedContainerColor = CaudexTheme.colors.k5,
                focusedTextColor = CaudexTheme.colors.k900,
                unfocusedTextColor = CaudexTheme.colors.k900,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = guideText,
                style = CaudexTheme.typography.caption1,
                color = CaudexTheme.colors.k400
            )

            Text(
                text = "${value.length}/$maxLength",
                style = CaudexTheme.typography.caption1,
                color = CaudexTheme.colors.k400
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CaudexTextFieldEmptyPreview() {
    CaudexTheme {
        CaudexTextField(
            title = "테스트",
            value = "",
            onValueChange = {},
            hint = "테스트",
            maxLength = 4,
            guideText = "테스트"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CaudexTextFieldTypingPreview() {
    CaudexTheme {
        var text by remember { mutableStateOf("까우") }

        CaudexTextField(
            title = "닉네임",
            value = text,
            onValueChange = { text = it },
            hint = "닉네임을 입력해주세요",
            maxLength = 4,
            guideText = "최대 4자까지 입력할 수 있어요"
        )
    }
}