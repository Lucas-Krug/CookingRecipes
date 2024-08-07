package de.lucas.cookingrecipes.auth.presentation.user.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.core.presentation.components.LoadingIndicator
import de.lucas.cookingrecipes.core.presentation.components.RoundedButton
import de.lucas.cookingrecipes.core.presentation.components.RoundedTextField
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.auth.presentation.components.PasswordTextField
import de.lucas.cookingrecipes.auth.presentation.user.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSheet(
    isLoading: Boolean,
    userState: UserState,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    onResetPassword: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            LoginSheetContent(
                isLoading = isLoading,
                userState = userState,
                onLogin = onLogin,
                onRegister = onRegister,
                onResetPassword = onResetPassword,
                onDismiss = onDismiss,
            )
        },
        windowInsets = WindowInsets.ime,
        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp),
        modifier = Modifier.fillMaxHeight(fraction = 0.85f)
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginSheetContent(
    isLoading: Boolean,
    userState: UserState,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    onResetPassword: () -> Unit,
    onDismiss: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var isEmpty by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    Box(modifier = Modifier.imePadding()) {
        Image(
            painter = painterResource(id = R.drawable.cooking_background),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.9f),
            contentScale = ContentScale.FillHeight
        )
        BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.TopCenter))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = Bold,
                textAlign = TextAlign.Start,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
            )
            Spacer(modifier = Modifier.height(24.dp))
            RoundedTextField(
                inputText = emailInput,
                isEmptyError = isEmpty,
                onInputChange = {
                    emailInput = it
                    isEmpty = false
                },
                userState = userState,
                labelRes = R.string.email,
                iconRes = R.drawable.ic_mail,
            )
            Spacer(modifier = Modifier.height(24.dp))
            PasswordTextField(
                inputText = passwordInput,
                isEmptyError = isEmpty,
                onInputChange = {
                    passwordInput = it
                    isEmpty = false
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.forgotPassword),
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    modifier = Modifier
                        .clickable { onResetPassword() }
                        .align(Alignment.CenterEnd)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            RoundedButton(labelRes = R.string.login) {
                focusManager.clearFocus()
                if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                    onLogin(emailInput, passwordInput)
                } else {
                    isEmpty = true
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.noAccount), color = Color.White)
                Text(
                    text = stringResource(id = R.string.register),
                    fontWeight = Bold,
                    color = Color.White,
                    modifier = Modifier.clickable { onRegister() }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.asGuest),
                fontSize = 16.sp,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = Color.White,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    }
    if (isLoading) LoadingIndicator()
}

@Preview(showBackground = true)
@Composable
private fun LoginSheetPreview() {
    CookingRecipesTheme {
        LoginSheetContent(
            isLoading = false,
            userState = UserState.Idle,
            onLogin = { _, _ -> },
            onRegister = {},
            onResetPassword = {},
            onDismiss = {}
        )
    }
}