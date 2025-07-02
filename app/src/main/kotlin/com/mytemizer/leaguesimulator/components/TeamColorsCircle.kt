package com.mytemizer.leaguesimulator.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun TeamColorsCircle(
    primaryColor: String?,
    secondaryColor: String?,
    modifier: Modifier = Modifier
) {
    val primary = parseHexColor(primaryColor) ?: Color.Gray
    val secondary = parseHexColor(secondaryColor) ?: Color.LightGray

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Draw left half (primary color)
        drawArc(
            color = primary,
            startAngle = 90f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw right half (secondary color)
        drawArc(
            color = secondary,
            startAngle = 270f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
    }
}


/**
 * Parse hex color string to Compose Color
 * Supports formats: #RGB, #RRGGBB, #AARRGGBB
 */
private fun parseHexColor(hexColor: String?): Color? {
    if (hexColor.isNullOrBlank()) return null

    return try {
        val cleanHex = hexColor.removePrefix("#")
        when (cleanHex.length) {
            3 -> {
                // #RGB -> #RRGGBB
                val r = cleanHex[0].toString().repeat(2)
                val g = cleanHex[1].toString().repeat(2)
                val b = cleanHex[2].toString().repeat(2)
                Color(android.graphics.Color.parseColor("#$r$g$b"))
            }
            6 -> {
                // #RRGGBB
                Color(android.graphics.Color.parseColor("#$cleanHex"))
            }
            8 -> {
                // #AARRGGBB
                Color(android.graphics.Color.parseColor("#$cleanHex"))
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}
