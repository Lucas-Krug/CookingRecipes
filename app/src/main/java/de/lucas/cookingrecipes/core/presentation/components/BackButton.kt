package de.lucas.cookingrecipes.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.main.theme.Grey65

@Composable
fun BackButton(modifier: Modifier, onClickBack: () -> Unit) {
    Button(
        onClick = onClickBack,
        colors = ButtonDefaults.buttonColors(
            containerColor = Grey65,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .defaultMinSize(40.dp)
            .clip(CircleShape),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "back",
        )
    }
}