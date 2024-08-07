package de.lucas.cookingrecipes.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme

@Composable
fun PasswordTextField(
    inputText: String,
    isEmptyError: Boolean,
    onInputChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = inputText,
            onValueChange = { onInputChange(it) },
            label = { Text(text = stringResource(R.string.password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) {
                    painterResource(R.drawable.ic_visible)
                } else {
                    painterResource(R.drawable.ic_invisible)
                }
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = description)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                cursorColor = MaterialTheme.colorScheme.onTertiary,
                errorContainerColor = MaterialTheme.colorScheme.tertiary,
                errorBorderColor = Color.Red,
                errorCursorColor = Color.Red,
                errorTextColor = Color.Red
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = ""
                )
            },
            isError = isEmptyError && inputText.isEmpty(),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusedState ->
                    focused = focusedState.isFocused
                },
        )
        if (isEmptyError && inputText.isEmpty()) {
            Text(
                text = stringResource(id = R.string.emptyFieldError),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPasswordTextField() {
    CookingRecipesTheme {
        PasswordTextField(
            inputText = "",
            isEmptyError = false,
            onInputChange = {})
    }
}