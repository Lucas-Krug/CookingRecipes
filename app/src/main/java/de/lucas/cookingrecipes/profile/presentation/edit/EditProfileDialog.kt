package de.lucas.cookingrecipes.profile.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.core.data.mock.Mock
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.core.presentation.components.DebugPlaceholderCircle
import de.lucas.cookingrecipes.core.presentation.components.EditTextField
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.core.presentation.util.isValidEmail

@Composable
fun EditProfileDialog(userData: UserData, onConfirm: (UserData) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(userData.name) }
    var email by remember { mutableStateOf(userData.email) }
    var picture by remember { mutableStateOf(userData.picture) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.editProfile)) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SubcomposeAsyncImage(
                    model = picture,
                    contentDescription = "Profile",
                    loading = {
                        DebugPlaceholderCircle(R.drawable.ic_woman_profile)
                    },
                    contentScale = ContentScale.FillBounds,
                    error = { painterResource(R.drawable.profile_default) },
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.height(8.dp))
                EditTextField(
                    initialValue = picture,
                    placeHolder = R.string.profileLink,
                    isRequired = false,
                    onValueChanged = { picture = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(40.dp))
                EditTextField(
                    initialValue = name ?: "",
                    placeHolder = R.string.username,
                    onValueChanged = { name = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EditTextField(
                    initialValue = email ?: "",
                    placeHolder = R.string.email,
                    onValueChanged = { email = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(40.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(24.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(
                                userData.copy(
                                    name = name,
                                    email = email,
                                    picture = picture,
                                )
                            )
                            onDismiss()
                        },
                        enabled = name?.isNotEmpty() == true && email?.isNotEmpty() == true && email!!.isValidEmail(),
                        shape = RoundedCornerShape(24.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Preview
@Composable
fun DefaultPreview() {
    CookingRecipesTheme {
        EditProfileDialog(userData = Mock().userData, onConfirm = {}, onDismiss = {})
    }
}