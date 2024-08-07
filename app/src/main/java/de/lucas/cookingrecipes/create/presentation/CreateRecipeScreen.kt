package de.lucas.cookingrecipes.create.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.core.data.mock.Mock
import de.lucas.cookingrecipes.core.presentation.components.BackButton
import de.lucas.cookingrecipes.core.presentation.components.DebugPlaceholderCircle
import de.lucas.cookingrecipes.core.presentation.components.EditTextField
import de.lucas.cookingrecipes.core.presentation.components.LoadingIndicator
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.recipe.data.Difficulty
import de.lucas.cookingrecipes.recipe.data.Recipe

@Composable
fun CreateRecipeScreen(
    uiState: CreateRecipeState,
    userData: UserData,
    onCreateRecipe: (Recipe) -> Unit,
    onCancelCreation: () -> Unit,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(Difficulty.VERY_EASY) }
    var portions by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf("") }
    val directions = remember { mutableStateListOf("") }
    var calories by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    val tags = remember { mutableStateListOf("") }
    var thumbnail by remember { mutableStateOf("") }
    var videolink by remember { mutableStateOf("") }
    var showCancelDialog by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.imePadding()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = stringResource(R.string.createRecipe),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.mandatory),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )
                SubcomposeAsyncImage(
                    model = thumbnail,
                    contentDescription = "Thumbnail",
                    loading = { DebugPlaceholderCircle(R.drawable.lentil_soup) },
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 64.dp, end = 64.dp, top = 24.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                EditTextField(
                    initialValue = thumbnail,
                    placeHolder = R.string.thumbnail,
                    onValueChanged = { thumbnail = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(24.dp))
                EditTextField(
                    initialValue = name,
                    placeHolder = R.string.recipeName,
                    onValueChanged = { name = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(24.dp))
                EditTextField(
                    initialValue = description,
                    placeHolder = R.string.description,
                    onValueChanged = { description = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(24.dp))
                EditTextField(
                    initialValue = portions,
                    placeHolder = R.string.portions,
                    isNumeric = true,
                    onValueChanged = { portions = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(24.dp))
                EditTextField(
                    initialValue = time,
                    placeHolder = R.string.preparationTime,
                    isNumeric = true,
                    onValueChanged = { time = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(40.dp))
                // Ingredient List Section
                ListSection(
                    header = R.string.ingredients,
                    placeHolder = R.string.ingredient,
                    onAddItem = { ingredients.add("") },
                    onIngredientChange = { ingredient, index ->
                        ingredients[index - 1] = ingredient
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                // Direction List Section
                ListSection(
                    header = R.string.directions,
                    placeHolder = R.string.direction,
                    onAddItem = { directions.add("") },
                    onIngredientChange = { direction, index ->
                        directions[index - 1] = direction
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                DifficultySection(onDifficultyChanged = { difficulty = it })
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.optional),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.nutrition),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    EditTextField(
                        initialValue = calories,
                        placeHolder = R.string.calories,
                        isNumeric = true,
                        isRequired = false,
                        onValueChanged = { calories = it },
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    EditTextField(
                        initialValue = fat,
                        placeHolder = R.string.fatInput,
                        isNumeric = true,
                        isRequired = false,
                        onValueChanged = { fat = it },
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    EditTextField(
                        initialValue = carbs,
                        placeHolder = R.string.carbsInput,
                        isNumeric = true,
                        isRequired = false,
                        onValueChanged = { carbs = it },
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    EditTextField(
                        initialValue = protein,
                        placeHolder = R.string.proteinInput,
                        isNumeric = true,
                        isRequired = false,
                        onValueChanged = { protein = it },
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                // Tag List Section
                ListSection(
                    header = R.string.tags,
                    placeHolder = R.string.tag,
                    isRequired = false,
                    onAddItem = { tags.add("") },
                    onIngredientChange = { tag, index ->
                        tags[index - 1] = tag
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.videoLink),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                EditTextField(
                    initialValue = videolink,
                    placeHolder = R.string.videoLink,
                    isRequired = false,
                    onValueChanged = { videolink = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(48.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onCreateRecipe(
                                Recipe(
                                    authorName = userData.name ?: "",
                                    name = name,
                                    description = description,
                                    difficulty = difficulty.value,
                                    portions = portions.toInt(),
                                    time = time.toInt(),
                                    ingredients = ingredients.toList(),
                                    directions = directions.toList(),
                                    calories = calories.toIntOrNull(),
                                    fat = fat.toIntOrNull(),
                                    carbs = carbs.toIntOrNull(),
                                    protein = protein.toIntOrNull(),
                                    tags = tags.toList(),
                                    thumbnail = thumbnail,
                                    videolink = videolink,
                                )
                            )
                        },
                        enabled = name.isNotEmpty() &&
                                ingredients.toList().isNotEmpty() &&
                                ingredients.toList().isNotEmpty() &&
                                directions.toList().first().isNotEmpty() &&
                                directions.toList().first().isNotEmpty() &&
                                thumbnail.isNotEmpty() &&
                                portions.isNotEmpty() &&
                                time.isNotEmpty(),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .width(160.dp)
                            .height(48.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.create),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (name.isNotEmpty() ||
                                ingredients.toList().isNotEmpty() ||
                                directions.toList().isNotEmpty() ||
                                thumbnail.isNotEmpty() ||
                                portions.isNotEmpty() ||
                                time.isNotEmpty()
                            ) {
                                showCancelDialog = true
                            } else {
                                onCancelCreation()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .width(160.dp)
                            .height(48.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        if (showCancelDialog) {
            CancelDialog(
                onDismissDialog = { showCancelDialog = false },
                onCancelCreation = onCancelCreation,
            )
        }
        BackButton(modifier = Modifier.padding(8.dp), onClickBack = {
            if (name.isNotEmpty() ||
                ingredients.toList().isEmpty() ||
                directions.toList().isEmpty() ||
                thumbnail.isNotEmpty() ||
                portions.isNotEmpty() ||
                time.isNotEmpty()
            ) {
                showCancelDialog = true
            } else {
                onCancelCreation()
            }
        })
        when (uiState) {
            is CreateRecipeState.Loading -> {
                LoadingIndicator()
            }

            is CreateRecipeState.Success -> {
                val message = stringResource(R.string.createRecipeSuccess)
                LaunchedEffect(Unit) {
                    onSuccess(message)
                }
            }

            is CreateRecipeState.Error -> {
                onError(stringResource(R.string.createRecipeError))
            }

            CreateRecipeState.Idle -> {}
        }
    }
}

@Composable
private fun ListSection(
    @StringRes header: Int,
    @StringRes placeHolder: Int,
    isRequired: Boolean = true,
    onAddItem: () -> Unit,
    onIngredientChange: (String, Int) -> Unit,
) {
    val title = stringResource(header) + if (isRequired) stringResource(R.string.star) else ""
    var index by remember { mutableIntStateOf(1) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .weight(1f),
        )
        Button(
            onClick = {
                index++
                onAddItem()
            },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp),
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    for (i in 1..index) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.index, i),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 8.dp),
            )
            EditTextField(
                initialValue = "",
                placeHolder = placeHolder,
                isRequired = isRequired,
                onValueChanged = { onIngredientChange(it, index) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
private fun CancelDialog(onDismissDialog: () -> Unit, onCancelCreation: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        text = {
            Text(
                text = stringResource(R.string.cancelMessage),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        confirmButton = {
            Button(onClick = onCancelCreation) {
                Text(text = stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissDialog,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(text = stringResource(R.string.no))
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DifficultySection(onDifficultyChanged: (Difficulty) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    Text(
        text = stringResource(R.string.difficulty) + stringResource(R.string.star),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        maxItemsInEachRow = 3,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Difficulty.entries.forEachIndexed { index, item ->
            Box(
                Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier
                    .clickable {
                        selectedIndex = index
                        onDifficultyChanged(Difficulty.entries[index])
                    }
                    .background(
                        if (selectedIndex == index) MaterialTheme.colorScheme.secondary else Color.Transparent
                    )
                    .padding(16.dp)
                ) {
                    Text(text = item.value)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateRecipeScreen() {
    CookingRecipesTheme {
        CreateRecipeScreen(
            uiState = CreateRecipeState.Success,
            userData = Mock().userData,
            onCreateRecipe = {},
            onCancelCreation = {},
            onSuccess = {},
            onError = {},
        )
    }
}

@Preview
@Composable
private fun PreviewCancelDialog() {
    CookingRecipesTheme {
        CancelDialog(onDismissDialog = {}, onCancelCreation = {})
    }
}