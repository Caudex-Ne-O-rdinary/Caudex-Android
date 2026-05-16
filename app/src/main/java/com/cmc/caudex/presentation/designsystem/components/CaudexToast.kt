package com.cmc.caudex.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.caudex.R
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme

@Composable
fun CaudexToast(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(CaudexTheme.colors.k600)
            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.check),
            contentDescription = "Check Icon",
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(4.5.dp))

        Text(
            text = message,
            style = CaudexTheme.typography.body3,
            color = CaudexTheme.colors.k5
        )
    }
}

@Preview
@Composable
private fun CaudexToastPreview() {
    CaudexTheme {
        Box(
            modifier = Modifier
                .background(CaudexTheme.colors.k50)
                .padding(24.dp)
        ) {
            CaudexToast(message = "변경사항이 저장되었습니다.")
        }
    }
}