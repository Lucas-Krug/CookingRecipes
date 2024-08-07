package de.lucas.cookingrecipes.details.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.core.data.mock.Mock
import de.lucas.cookingrecipes.recipe.data.Rating
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.core.presentation.modifier.bounceClick
import de.lucas.cookingrecipes.details.presentation.comment.CommentSection
import de.lucas.cookingrecipes.core.presentation.components.BackButton
import de.lucas.cookingrecipes.core.presentation.components.DebugPlaceholderBox
import de.lucas.cookingrecipes.recipe.presentation.components.RecipeInfo
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.main.theme.Grey65
import de.lucas.cookingrecipes.main.theme.White70
import de.lucas.cookingrecipes.details.presentation.videoplayer.VideoPlayer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeDetailsScreen(
    isLoggedIn: Boolean,
    recipe: Recipe,
    ratingList: List<Rating>,
    category: String,
    favorite: Boolean,
    onClickFavorite: (Boolean) -> Unit,
    onRatingChanged: (Float) -> Unit,
    onComment: (String) -> Unit,
    onClickBack: () -> Unit,
    showLoginSheet: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var isFavorite by remember { mutableStateOf(favorite) }
    var showRating by remember { mutableStateOf(ratingList.any { it.id == recipe.id }) }
    var userRating by remember {
        mutableFloatStateOf(
            ratingList.filter { it.id == recipe.id }.getOrNull(0)?.rating ?: 0f
        )
    }
    val outState = rememberScrollState()
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.imePadding()
        ) {
            SubcomposeAsyncImage(
                model = recipe.thumbnail,
                contentDescription = "",
                loading = {
                    DebugPlaceholderBox(R.drawable.lentil_soup)
                },
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .weight(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/$category/${recipe.thumbnail}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 500)
                        },
                    ),
            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White,
                ),
                shape = RectangleShape,
                modifier = Modifier.weight(3f),
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp),
                    ) {
                        Text(
                            text = recipe.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                        )
                        FavoriteButton(
                            isFavorite = isFavorite,
                            setIsFavorite = {
                                if (isLoggedIn) {
                                    isFavorite = !isFavorite
                                    onClickFavorite(isFavorite)
                                } else {
                                    showLoginSheet()
                                }
                            },
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                    RecipeInfo(recipe = recipe, modifier = Modifier.padding(bottom = 16.dp))
                    RatingSection(
                        recipe = recipe,
                        showRating = showRating,
                        userRating = userRating,
                        onRatingChanged = onRatingChanged,
                        onUserRatingChanged = { userRating = it },
                        onShowRating = {
                            if (isLoggedIn) {
                                showRating = true
                            } else {
                                showLoginSheet()
                            }
                        },
                    )
                    DescriptionSection(description = recipe.description)
                    IngredientList(
                        items = recipe.ingredients,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )
                    DirectionSection(recipe)
                    NutritionSection(recipe)
                    if (recipe.videolink.isNotEmpty()) VideoPlayer(recipe.videolink)
                    Spacer(modifier = Modifier.height(40.dp))
                    if (recipe.tags.isNotEmpty() && recipe.tags.first().isNotEmpty()) {
                        TagsList(
                            tags = recipe.tags,
                            modifier = Modifier
                                .heightIn(max = 1000.dp)
                                .nestedScroll(connection = object : NestedScrollConnection {
                                    override fun onPreScroll(
                                        available: Offset,
                                        source: NestedScrollSource
                                    ): Offset {
                                        if (outState.canScrollForward && available.y < 0) {
                                            val consumed = outState.dispatchRawDelta(-available.y)
                                            return Offset(x = 0f, y = -consumed)
                                        }
                                        return Offset.Zero
                                    }
                                })
                                .padding(bottom = 32.dp),
                        )
                    }
                    Text(
                        text = stringResource(R.string.authorSection, recipe.authorName),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    CommentSection(
                        comments = recipe.comments ?: emptyList(),
                        isLoggedIn = isLoggedIn,
                        onComment = { comment ->
                            if (isLoggedIn) onComment(comment) else showLoginSheet()
                        },
                    )
                }
            }
        }
        BackButton(modifier = Modifier.padding(8.dp), onClickBack = onClickBack)
    }
}

@Composable
private fun RatingSection(
    recipe: Recipe,
    showRating: Boolean,
    userRating: Float,
    onRatingChanged: (Float) -> Unit,
    onUserRatingChanged: (Float) -> Unit,
    onShowRating: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RatingBar(
            value = recipe.ratingSum.takeIf { it != 0f }?.div(recipe.ratedNumber) ?: 0f,
            style = RatingBarStyle.Stroke(),
            spaceBetween = 0.dp,
            size = 24.dp,
            stepSize = StepSize.HALF,
            onValueChange = { },
            onRatingChanged = { },
        )
        Text(
            text = if (recipe.ratedNumber != 0) {
                "${"%.1f".format(recipe.ratingSum.div(recipe.ratedNumber))} (${recipe.ratedNumber})"
            } else {
                "(0)"
            },
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    if (showRating) {
        Text(
            text = stringResource(R.string.yourRating),
            color = White70,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            RatingBar(
                value = userRating,
                style = RatingBarStyle.Stroke(),
                spaceBetween = 0.dp,
                size = 24.dp,
                stepSize = StepSize.HALF,
                onValueChange = { onUserRatingChanged(it) },
                onRatingChanged = { onRatingChanged(it) },
            )
            Text(
                text = userRating.toString(),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    } else {
        Button(
            onClick = { onShowRating() },
            colors = ButtonDefaults.buttonColors(containerColor = Grey65),
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.rate),
                color = White70,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    setIsFavorite: () -> Unit,
    modifier: Modifier,
) {
    Box(modifier) {
        Button(
            onClick = {},
            shape = CircleShape,
            elevation = null,
            border = BorderStroke(2.dp, Color.White),
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(1.dp),
        ) {
            Icon(
                painter = painterResource(
                    if (isFavorite) {
                        R.drawable.ic_favorite_filled
                    } else {
                        R.drawable.ic_favorite_nofill
                    }
                ),
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(32.dp)
                    .bounceClick { setIsFavorite() }
            )
        }

    }
}

@Composable
private fun DescriptionSection(description: String) {
    Text(
        text = stringResource(R.string.description),
        color = Color.White,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Text(
        text = description,
        color = Color.White,
        modifier = Modifier.padding(bottom = 24.dp),
    )
}

@Composable
private fun IngredientList(
    modifier: Modifier = Modifier,
    items: List<String>,
) {
    Text(
        text = stringResource(R.string.ingredients),
        color = Color.White,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Column(modifier = modifier) {
        items.forEach {
            var checked by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked, onCheckedChange = { checked = it })
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun DirectionSection(recipe: Recipe) {
    Text(
        text = stringResource(R.string.directions),
        color = Color.White,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    recipe.directions.forEachIndexed { index, direction ->
        Row {
            Text(
                text = stringResource(R.string.index, index + 1), color = Color.White,
                modifier = Modifier.padding(end = 4.dp),
            )
            Text(
                text = direction, color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp),
            )
        }
    }
}

@Composable
private fun NutritionSection(recipe: Recipe) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.nutrition),
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(end = 4.dp),
        )
        Text(
            text = stringResource(R.string.perServing),
            color = White70,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.offset(y = (-4).dp),
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .height(IntrinsicSize.Min)
    ) {
        NutritionItem(
            stringRes = R.string.calories,
            value = if (recipe.calories == 0 || recipe.calories == null) "-" else recipe.calories.toString(),
        )
        NutritionItem(
            stringRes = R.string.fat,
            value = if (recipe.calories == 0 || recipe.calories == null) "-" else "${recipe.fat}g",
        )
        NutritionItem(
            stringRes = R.string.carbs,
            value = if (recipe.calories == 0 || recipe.calories == null) "-" else "${recipe.carbs}g",
        )
        NutritionItem(
            stringRes = R.string.protein,
            value = if (recipe.calories == 0 || recipe.calories == null) "-" else "${recipe.protein}g",
            isLastItem = true,
        )
    }
}

@Composable
private fun NutritionItem(@StringRes stringRes: Int, value: String, isLastItem: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(stringRes), color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Text(text = value, color = Color.White)
    }
    if (!isLastItem) VerticalDivider(
        thickness = 1.dp,
        color = Color.Gray,
        modifier = Modifier.fillMaxHeight()
    )
}

@Composable
private fun TagItem(tag: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Text(
            text = tag.uppercase(),
            textAlign = TextAlign.Center,
            color = White70,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun TagsList(
    modifier: Modifier = Modifier,
    tags: List<String>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
    ) {
        items(tags) { tag ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                TagItem(tag = tag)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SharedTransitionScope.PreviewRecipeDetailsScreen() {
    CookingRecipesTheme {
        AnimatedVisibility(visible = true) {
            RecipeDetailsScreen(
                isLoggedIn = true,
                recipe = Mock().recipe,
                ratingList = Mock().ratings,
                favorite = false,
                category = "",
                onClickFavorite = {},
                onRatingChanged = {},
                onComment = {},
                onClickBack = {},
                showLoginSheet = {},
                animatedVisibilityScope = this,
            )
        }
    }
}
