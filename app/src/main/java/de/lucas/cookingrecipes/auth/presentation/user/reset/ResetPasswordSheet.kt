package de.lucas.cookingrecipes.auth.presentation.user.reset

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.auth.presentation.user.UserState
import de.lucas.cookingrecipes.core.presentation.components.LoadingIndicator
import de.lucas.cookingrecipes.core.presentation.components.RoundedButton
import de.lucas.cookingrecipes.core.presentation.components.RoundedTextField
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordSheet(
    isLoading: Boolean,
    userState: UserState,
    onReset: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            ResetPasswordSheetContent(
                isLoading = isLoading,
                userState = userState,
                onReset = onReset
            )
        },
        windowInsets = WindowInsets.ime,
        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp),
        modifier = Modifier.fillMaxHeight(fraction = 0.85f),
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResetPasswordSheetContent(
    isLoading: Boolean,
    userState: UserState,
    onReset: (String) -> Unit,
) {
    var isEmpty by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Box {
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
        ) {
            Text(
                text = stringResource(id = R.string.resetPassword),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(24.dp))
            RoundedTextField(
                inputText = emailInput,
                userState = userState,
                isEmptyError = isEmpty,
                onInputChange = {
                    emailInput = it
                    isEmpty = false
                },
                labelRes = R.string.email,
                iconRes = R.drawable.ic_mail
            )
            Spacer(modifier = Modifier.height(40.dp))
            RoundedButton(labelRes = R.string.reset, onClick = {
                focusManager.clearFocus()
                onReset(emailInput)
                if (emailInput.isEmpty()) {
                    isEmpty = true
                }
            })
        }
    }
    if (isLoading) LoadingIndicator()
}

@Preview
@Composable
private fun PreviewResetPasswordSheet() {
    CookingRecipesTheme {
        ResetPasswordSheetContent(
            isLoading = false,
            userState = UserState.Idle,
            onReset = {},
        )
    }
}