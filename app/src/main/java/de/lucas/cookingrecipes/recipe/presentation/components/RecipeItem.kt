package de.lucas.cookingrecipes.recipe.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.core.data.mock.Mock
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.core.presentation.components.DebugPlaceholderBox
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.main.theme.Grey65

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeItem(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    category: String,
    top10Padding: Int,
    isRecommended: Boolean,
    onClickRecipe: (Recipe) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .width(288.dp)
            .height(280.dp)
            .clickable { onClickRecipe(recipe) },
    ) {
        Box {
            Column {
                Box {
                    SubcomposeAsyncImage(
                        model = recipe.thumbnail,
                        contentDescription = recipe.name,
                        loading = {
                            DebugPlaceholderBox(R.drawable.lentil_soup)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .sharedElement(
                                state = rememberSharedContentState(key = "image/$category/${recipe.thumbnail}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 500)
                                },
                            ),
                        contentScale = ContentScale.FillBounds,
                    )
                    Card(
                        shape = RoundedCornerShape(topStart = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Grey65),
                        modifier = Modifier.align(BottomEnd),
                    ) {
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 4.dp,
                                top = 4.dp,
                                bottom = 4.dp,
                            ),
                        ) {
                            Text(
                                text = (recipe.comments?.size ?: 0).toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_comment),
                                tint = Color.White,
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
                RecipeInfo(recipe = recipe, modifier = Modifier.padding(8.dp))
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )
                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                ) {
                    RatingBar(
                        value = recipe.ratingSum.takeIf { it != 0f }?.div(recipe.ratedNumber) ?: 0f,
                        style = RatingBarStyle.Stroke(),
                        spaceBetween = 0.dp,
                        size = 24.dp,
                        isIndicator = true,
                        stepSize = StepSize.HALF,
                        onValueChange = {},
                        onRatingChanged = {},
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
            }
            if (recipe.top10 != 0 && isRecommended) {
                Top10Indicator(recipe = recipe, top10Padding = top10Padding)
            }
        }
    }
}

@Composable
fun RecipeInfo(recipe: Recipe, modifier: Modifier = Modifier) {
    Row(modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .border(1.dp, Color.Gray)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = "timer",
            )
            Text(
                text = stringResource(R.string.cookingTime, recipe.time.toString()),
                modifier = Modifier.padding(start = 4.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .border(1.dp, Color.Gray)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_skillet),
                contentDescription = "difficulty",
            )
            Text(
                text = recipe.difficulty,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .border(1.dp, Color.Gray)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flatware),
                contentDescription = "portions",
            )
            Text(
                text = recipe.portions.toString(),
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
private fun Top10Indicator(recipe: Recipe, top10Padding: Int) {
    Box(Modifier.size(64.dp)) {
        DiagonalSplitCanvas(
            color1 = MaterialTheme.colorScheme.secondary,
            color2 = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = recipe.top10.toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp, start = top10Padding.dp),
        )
    }
}

@Composable
private fun DiagonalSplitCanvas(
    color1: Color,
    color2: Color,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        drawDiagonalSplit(size, color1, color2)
    }
}

private fun DrawScope.drawDiagonalSplit(
    canvasSize: androidx.compose.ui.geometry.Size,
    color1: Color,
    color2: Color,
) {
    val path1 = Path().apply {
        moveTo(0f, 0f)
        lineTo(canvasSize.width, 0f)
        lineTo(0f, canvasSize.height)
        close()
    }
    drawPath(
        path = path1,
        color = color1
    )

    val path2 = Path().apply {
        moveTo(canvasSize.width, 0f)
        lineTo(canvasSize.width, canvasSize.height)
        lineTo(0f, canvasSize.height)
        close()
    }
    drawPath(
        path = path2,
        color = color2
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SharedTransitionScope.PreviewRecipeItem() {
    CookingRecipesTheme {
        AnimatedVisibility(visible = true) {
            RecipeItem(
                recipe = Mock().recipe,
                category = "",
                top10Padding = 12,
                isRecommended = true,
                onClickRecipe = {},
                animatedVisibilityScope = this,
                modifier = Modifier,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTop10Indicator() {
    CookingRecipesTheme {
        Top10Indicator(recipe = Mock().recipe, top10Padding = 12)
    }
}