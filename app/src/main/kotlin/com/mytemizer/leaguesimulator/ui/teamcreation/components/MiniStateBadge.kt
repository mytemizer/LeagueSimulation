package com.mytemizer.leaguesimulator.ui.teamcreation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun MiniStatBadge(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.toString(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                value >= 85 -> Color(0xFF4CAF50)
                value >= 70 -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            }
        )
    }
}

@Preview
@Composable
private fun MiniStatBadgePreview() {
    MiniStatBadge(label = "Attack", value = 80)
}
