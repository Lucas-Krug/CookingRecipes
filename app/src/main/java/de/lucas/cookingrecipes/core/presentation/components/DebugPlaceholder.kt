package de.lucas.cookingrecipes.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.core.presentation.modifier.shimmerBackground

@Composable
fun DebugPlaceholderBox(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        Image(
            painter = painterResource(debugPreview),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
    } else {
        Box(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(240.dp)
                .shimmerBackground()
        )
    }

@Composable
fun DebugPlaceholderCircle(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        Image(
            painter = painterResource(debugPreview),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
    } else {
        Box(
            Modifier
                .clip(CircleShape)
                .size(56.dp)
                .shimmerBackground()
        )
    }