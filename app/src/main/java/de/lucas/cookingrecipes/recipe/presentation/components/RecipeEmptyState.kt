package de.lucas.cookingrecipes.recipe.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme

@Composable
fun RecipeEmptyState(onClickReload: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp),
    ) {
        Text(text = stringResource(R.string.emptyStateRecipeMessage), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onClickReload() }) {
            Text(text = stringResource(R.string.tryAgain))
        }
    }
}

@Preview
@Composable
private fun PreviewRecipeEmptyState() {
    CookingRecipesTheme {
        RecipeEmptyState(onClickReload = {})
    }
}