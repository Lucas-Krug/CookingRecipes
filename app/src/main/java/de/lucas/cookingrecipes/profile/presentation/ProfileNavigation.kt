package de.lucas.cookingrecipes.profile.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.toRoute
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.core.data.serialization.serializableType
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

@Serializable
data class Profile(val userData: UserData) {
    companion object {
        val typeMap = mapOf(typeOf<UserData>() to serializableType<UserData>())

        fun from(savedStateHandle: SavedStateHandle) = savedStateHandle.toRoute<Profile>(typeMap)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileNavigation(
    logout: () -> Unit,
    updateUserData: () -> Unit,
    onClickBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val userData = viewModel.user.collectAsStateWithLifecycle()
    ProfileScreen(
        userData = userData.value,
        animatedVisibilityScope = animatedVisibilityScope,
        onConfirmEditProfile = { data ->
            viewModel.saveUserData(data)
            updateUserData()
        },
        logout = logout,
        onClickBack = onClickBack,
    )
}