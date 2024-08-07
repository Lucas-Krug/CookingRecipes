package de.lucas.cookingrecipes.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButton(@StringRes labelRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .width(200.dp)
            .height(48.dp)
    ) {
        Text(text = stringResource(labelRes), style = MaterialTheme.typography.titleMedium)
    }
}