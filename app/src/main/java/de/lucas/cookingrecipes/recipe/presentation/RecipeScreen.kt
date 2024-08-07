package de.lucas.cookingrecipes.recipe.presentation

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.auth.presentation.user.UserState
import de.lucas.cookingrecipes.core.data.mock.Mock
import de.lucas.cookingrecipes.core.presentation.components.DebugPlaceholderCircle
import de.lucas.cookingrecipes.core.presentation.modifier.shimmerBackground
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.main.theme.Grey20
import de.lucas.cookingrecipes.main.theme.transparent
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.recipe.presentation.components.RecipeEmptyState
import de.lucas.cookingrecipes.recipe.presentation.components.RecipeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeScreen(
    uiState: RecipeState,
    recommendationList: List<Recipe>,
    userRecipes: List<Recipe>,
    favoriteList: List<Recipe>,
    tagLists: Map<String, List<Recipe>>,
    noTagList: List<Recipe>,
    userState: UserState,
    snackbarHostState: SnackbarHostState,
    navigateToProfile: () -> Unit,
    showLoginSheet: () -> Unit,
    onCreateRecipe: () -> Unit,
    logout: () -> Unit,
    onClickReload: () -> Unit,
    onClickRecipe: (Recipe, String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (userState is UserState.Success) {
                        onCreateRecipe()
                    } else {
                        showLoginSheet()
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
    ) { innerPadding ->
        Box {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .background(color = MaterialTheme.colorScheme.primaryContainer)

                ) {
                    Text(
                        text = stringResource(R.string.appName),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp),
                    )
                    ProfileFab(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        userState = userState,
                        snackbarHostState = snackbarHostState,
                        scope = scope,
                        context = context,
                        animatedVisibilityScope = animatedVisibilityScope,
                        navigateToProfile = navigateToProfile,
                        showLoginSheet = showLoginSheet,
                        logout = logout,
                    )
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    when (uiState) {
                        is RecipeState.Loading -> {
                            SkeletonItemList()
                        }

                        is RecipeState.Success -> {
                            val recommendationTitle = stringResource(R.string.recommendations)
                            RecipeList(
                                title = recommendationTitle,
                                recipes = recommendationList,
                                isRecommended = true,
                                onClickRecipe = { recipe ->
                                    onClickRecipe(recipe, recommendationTitle)
                                },
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            if (favoriteList.isNotEmpty()) {
                                val myFavoritesTitle = stringResource(R.string.myFavorites)
                                RecipeList(
                                    title = myFavoritesTitle,
                                    recipes = favoriteList,
                                    onClickRecipe = { recipe ->
                                        onClickRecipe(recipe, myFavoritesTitle)
                                    },
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            if (userRecipes.isNotEmpty()) {
                                val myRecipesTitle = stringResource(R.string.myRecipes)
                                RecipeList(
                                    title = myRecipesTitle,
                                    recipes = userRecipes,
                                    onClickRecipe = { recipe ->
                                        onClickRecipe(recipe, myRecipesTitle)
                                    },
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                                    .background(Grey20)
                            ) {
                                Text(
                                    text = stringResource(R.string.specificTitle),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(
                                        start = 20.dp,
                                        top = 16.dp,
                                        bottom = 16.dp,
                                    ),
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            tagLists.forEach { tagList ->
                                RecipeList(
                                    title = tagList.key,
                                    recipes = tagList.value,
                                    onClickRecipe = { recipe ->
                                        onClickRecipe(recipe, tagList.key)
                                    },
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            val myFavoritesTitle = stringResource(R.string.others)
                            RecipeList(
                                title = myFavoritesTitle,
                                recipes = noTagList,
                                onClickRecipe = { recipe ->
                                    onClickRecipe(recipe, myFavoritesTitle)
                                },
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                            Spacer(modifier = Modifier.height(88.dp))
                        }

                        is RecipeState.Error -> {
                            RecipeEmptyState(onClickReload)
                        }

                        RecipeState.Idle -> {}
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.RecipeList(
    title: String,
    recipes: List<Recipe>,
    isRecommended: Boolean = false,
    onClickRecipe: (Recipe) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(start = 24.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(recipes) { index, recipe ->
                val isLastItem = index == recipes.lastIndex
                RecipeItem(
                    recipe = recipe,
                    category = title,
                    top10Padding = if (isLastItem) 4 else 12,
                    isRecommended = isRecommended,
                    onClickRecipe = onClickRecipe,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = when (index) {
                        0 -> Modifier.padding(start = 24.dp)
                        recipes.lastIndex -> Modifier.padding(end = 24.dp)
                        else -> Modifier
                    },
                )
            }
        }
    }
}

@Composable
private fun SkeletonItemList() {
    repeat(3) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(240.dp)
                    .height(32.dp)
                    .shimmerBackground()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .width(288.dp)
                        .height(280.dp)
                        .shimmerBackground()
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .width(288.dp)
                        .height(280.dp)
                        .shimmerBackground()
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ProfileFab(
    modifier: Modifier,
    userState: UserState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToProfile: () -> Unit,
    showLoginSheet: () -> Unit,
    logout: () -> Unit,
) {
    Box(modifier.padding(end = 16.dp)) {
        FloatingActionButton(
            onClick = {
                if (userState is UserState.Success) {
                    (userState).userData?.id?.let {
                        navigateToProfile()
                    } ?: scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.profileDataError),
                            duration = SnackbarDuration.Indefinite,
                            withDismissAction = true,
                        )
                        logout()
                    }
                } else {
                    showLoginSheet()
                }
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(48.dp),
        ) {
            if (userState is UserState.Success) {
                SubcomposeAsyncImage(
                    model = (userState).userData?.picture,
                    contentDescription = "Profile",
                    loading = {
                        DebugPlaceholderCircle(R.drawable.ic_woman_profile)
                    },
                    contentScale = ContentScale.FillBounds,
                    error = { painterResource(R.drawable.profile_default) },
                    modifier = Modifier
                        .background(transparent)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image/${(userState).userData?.picture}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 700)
                            },
                        )
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.profile_default),
                    contentDescription = "Profile",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSkeletonList() {
    CookingRecipesTheme {
        SkeletonItemList()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SharedTransitionScope.PreviewRecipeScreen() {
    CookingRecipesTheme {
        AnimatedVisibility(visible = true) {
            RecipeScreen(
                uiState = RecipeState.Success(Mock().recipeList),
                recommendationList = Mock().recipeList,
                userRecipes = Mock().recipeList,
                favoriteList = Mock().recipeList,
                tagLists = Mock().tagList,
                noTagList = Mock().recipeList,
                animatedVisibilityScope = this,
                userState = UserState.Success(Mock().userData),
                snackbarHostState = SnackbarHostState(),
                onClickReload = {},
                onClickRecipe = { _, _ -> },
                onCreateRecipe = {},
                navigateToProfile = {},
                showLoginSheet = {},
                logout = {},
            )
        }
    }
}
