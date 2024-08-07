package de.lucas.cookingrecipes.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.main.theme.Grey
import de.lucas.cookingrecipes.main.theme.White70
import de.lucas.cookingrecipes.auth.presentation.user.PasswordState

@Composable
fun PasswordValidator(passwordState: PasswordState) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .height(4.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .background(if (i <= passwordState.index) passwordState.color else Grey)
                )
                if (i != 4) Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(passwordState.labelRes),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            color = if (passwordState == PasswordState.DEFAULT) {
                White70
            } else {
                passwordState.color
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun PreviewPasswordValidatorDefault() {
    CookingRecipesTheme {
        PasswordValidator(passwordState = PasswordState.DEFAULT)
    }
}

@Preview
@Composable
private fun PreviewPasswordValidatorTooWeak() {
    CookingRecipesTheme {
        PasswordValidator(passwordState = PasswordState.VERY_WEAK)
    }
}

@Preview
@Composable
private fun PreviewPasswordValidatorWeak() {
    CookingRecipesTheme {
        PasswordValidator(passwordState = PasswordState.WEAK)
    }
}

@Preview
@Composable
private fun PreviewPasswordValidatorGood() {
    CookingRecipesTheme {
        PasswordValidator(passwordState = PasswordState.GOOD)
    }
}

@Preview
@Composable
private fun PreviewPasswordValidatorStrong() {
    CookingRecipesTheme {
        PasswordValidator(passwordState = PasswordState.STRONG)
    }
}