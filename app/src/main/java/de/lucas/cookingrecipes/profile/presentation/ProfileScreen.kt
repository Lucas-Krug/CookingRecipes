package de.lucas.cookingrecipes.profile.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.main.theme.Red
import de.lucas.cookingrecipes.main.theme.White70
import de.lucas.cookingrecipes.main.theme.transparent
import de.lucas.cookingrecipes.profile.presentation.edit.EditProfileDialog

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileScreen(
    userData: UserData,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onConfirmEditProfile: (UserData) -> Unit,
    logout: () -> Unit,
    onClickBack: () -> Unit,
) {
    var showEditDialog by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 80.dp))
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(64.dp))
                    SubcomposeAsyncImage(
                        model = userData.picture,
                        contentDescription = "Profile",
                        loading = {
                            DebugPlaceholderCircle(R.drawable.ic_woman_profile)
                        },
                        contentScale = ContentScale.FillBounds,
                        error = { painterResource(R.drawable.profile_default) },
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "image/${userData.picture}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 700)
                                },
                            )
                            .size(128.dp)
                            .clip(CircleShape),
                    )
                    Text(
                        text = userData.name ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 8.dp),
                    )
                    Text(
                        text = userData.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White70,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                    )
                }
                BackButton(modifier = Modifier.padding(8.dp), onClickBack = onClickBack)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                ProfileStats(userData)
                Spacer(modifier = Modifier.height(40.dp))
                ProfileButton(
                    text = R.string.editProfile,
                    icon = R.drawable.ic_edit,
                    onClick = { showEditDialog = true },
                )
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = logout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.width(216.dp),
                ) {
                    Row(Modifier.padding(8.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = "",
                            tint = Red,
                            modifier = Modifier.padding(end = 16.dp),
                        )
                        Text(
                            text = stringResource(R.string.logout),
                            style = MaterialTheme.typography.titleMedium,
                            color = Red,
                        )
                    }
                }
            }
        }
        if (showEditDialog) {
            EditProfileDialog(
                userData = userData,
                onConfirm = { data -> onConfirmEditProfile(data) },
                onDismiss = { showEditDialog = false }
            )
        }
    }
}

@Composable
private fun ProfileStats(userData: UserData) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(40.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Black,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(transparent)
                .padding(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.recipeStat), color = Color.White)
                Text(text = (userData.myRecipes.size).toString(), color = Color.White)
            }
            VerticalDivider(
                thickness = 1.dp,
                color = White70,
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 32.dp),
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.commentStat), color = Color.White)
                Text(text = (userData.commentCount).toString(), color = Color.White)
            }
        }
    }
}

@Composable
private fun ProfileButton(@StringRes text: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .width(216.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black,
            ),
    ) {
        Row(Modifier.padding(8.dp)) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                Modifier.padding(end = 16.dp),
            )
            Text(text = stringResource(text), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SharedTransitionScope.ProfileScreenPreview() {
    CookingRecipesTheme {
        AnimatedVisibility(visible = true) {
            ProfileScreen(
                userData = Mock().userData,
                animatedVisibilityScope = this,
                onConfirmEditProfile = {},
                logout = {},
                onClickBack = {},
            )
        }
    }
}

@Preview
@Composable
private fun ProfileStatsPreview() {
    CookingRecipesTheme {
        ProfileStats(userData = Mock().userData)
    }
}