package de.lucas.cookingrecipes.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.profile.presentation.Profile
import de.lucas.cookingrecipes.profile.presentation.ProfileNavigation
import de.lucas.cookingrecipes.recipe.presentation.RecipeNavigation
import de.lucas.cookingrecipes.recipe.presentation.RecipeScreen
import de.lucas.cookingrecipes.create.presentation.CreateRecipe
import de.lucas.cookingrecipes.create.presentation.CreateRecipeNavigation
import de.lucas.cookingrecipes.details.presentation.RecipeDetail
import de.lucas.cookingrecipes.details.presentation.RecipeDetailNavigation
import de.lucas.cookingrecipes.auth.presentation.user.login.LoginSheet
import de.lucas.cookingrecipes.auth.presentation.user.reset.ResetPasswordSheet
import de.lucas.cookingrecipes.auth.presentation.user.signup.SignUpSheet
import de.lucas.cookingrecipes.auth.presentation.user.UserState
import de.lucas.cookingrecipes.auth.presentation.user.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen() {
    val viewModel: UserViewModel = koinViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    var sheetState by remember { mutableStateOf(SheetState.NONE) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showProfileFab by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Scaffold(snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }) { innerPadding ->
            LoginNavigation(
                viewModel = viewModel,
                isLoading = userState is UserState.Loading,
                userState = userState,
                sheetType = sheetState,
                changeSheetState = { state -> sheetState = state }
            )
            Box {
                SharedTransitionLayout {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = RecipeScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<RecipeScreen> {
                            RecipeNavigation(
                                onClickRecipe = { recipe, category ->
                                    if (recipe.key != null) {
                                        navController.navigate(RecipeDetail(recipe.key, category))
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = context.getString(R.string.recipeDataError),
                                                duration = SnackbarDuration.Indefinite,
                                                withDismissAction = true,
                                            )
                                        }
                                    }
                                },
                                userState = userState,
                                snackbarHostState = snackbarHostState,
                                userRecipes = userState.takeIf { it is UserState.Success }
                                    ?.let { (it as UserState.Success).userData }?.myRecipes
                                    ?: emptyList(),
                                favoriteListIds = userState.takeIf { it is UserState.Success }
                                    ?.let { (it as UserState.Success).userData }?.favorites
                                    ?: emptyList(),
                                showLoginSheet = { sheetState = SheetState.LOGIN },
                                onCreateRecipe = { navController.navigate(CreateRecipe) },
                                navigateToProfile = { navController.navigate(Profile((userState as UserState.Success).userData!!)) },
                                logout = { viewModel.logoutUser() },
                                animatedVisibilityScope = this@composable,
                            )
                        }
                        composable<RecipeDetail> { backStackEntry ->
                            val key = backStackEntry.arguments?.getString("key")
                            val category = backStackEntry.arguments?.getString("category")
                            Timber.e(category)
                            RecipeDetailNavigation(
                                key = key ?: "",
                                userData = userState.takeIf { it is UserState.Success }
                                    ?.let { (it as UserState.Success).userData },
                                category = category ?: "",
                                onClickBack = { navController.popBackStack() },
                                showLoginSheet = { sheetState = SheetState.LOGIN },
                                animatedVisibilityScope = this,
                            )
                        }
                        composable<CreateRecipe> {
                            CreateRecipeNavigation(
                                userData = (userState as UserState.Success).userData,
                                onCancelCreation = { navController.popBackStack() },
                                onSuccess = { message ->
                                    navController.popBackStack()
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                },
                                onError = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                },
                            )
                        }
                        composable<Profile>(
                            typeMap = Profile.typeMap
                        ) { _ ->
                            ProfileNavigation(
                                logout = {
                                    viewModel.logoutUser()
                                    navController.popBackStack()
                                },
                                updateUserData = {
                                    viewModel.getUserFromDb()
                                },
                                onClickBack = {
                                    navController.popBackStack()
                                    showProfileFab = true
                                },
                                animatedVisibilityScope = this,
                            )
                        }
                    }
                }
            }
        }
    }


    when (userState) {
        is UserState.Success -> {
            sheetState = SheetState.NONE
        }

        is UserState.Error -> {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar((userState as UserState.Error).message ?: "")
            }
        }

        else -> {}
    }
}

@Composable
private fun LoginNavigation(
    viewModel: UserViewModel,
    isLoading: Boolean,
    userState: UserState,
    sheetType: SheetState,
    changeSheetState: (SheetState) -> Unit
) {
    when (sheetType) {
        SheetState.LOGIN -> {
            val errorMessage = stringResource(R.string.loginErrorMessage)
            LoginSheet(
                isLoading = isLoading,
                userState = userState,
                onLogin = { email, password ->
                    viewModel.loginUser(email, password, errorMessage)
                },
                onRegister = { changeSheetState(SheetState.SIGNUP) },
                onResetPassword = { changeSheetState(SheetState.RESET) },
                onDismiss = { changeSheetState(SheetState.NONE) },
            )
        }

        SheetState.SIGNUP -> {
            val registrationErrorMessage = stringResource(R.string.registrationErrorMessage)
            SignUpSheet(
                isLoading = isLoading,
                userState = userState,
                validatePassword = viewModel::validatePassword,
                passwordState = viewModel.passwordState.value,
                onRegister = { email, password, username ->
                    viewModel.signUpUser(
                        email = email,
                        password = password,
                        username = username,
                        errorMessage = registrationErrorMessage,
                    )
                },
                onDismiss = { changeSheetState(SheetState.NONE) },
            )
        }

        SheetState.RESET -> {
            ResetPasswordSheet(
                isLoading = isLoading,
                userState = userState,
                onReset = viewModel::resetPassword,
                onDismiss = { changeSheetState(SheetState.NONE) },
            )
        }

        SheetState.NONE -> {}
    }
}

enum class SheetState {
    NONE, LOGIN, SIGNUP, RESET
}