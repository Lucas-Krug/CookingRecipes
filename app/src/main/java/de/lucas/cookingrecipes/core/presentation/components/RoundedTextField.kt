package de.lucas.cookingrecipes.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.auth.presentation.user.UserState

@Composable
fun RoundedTextField(
    inputText: String,
    isEmptyError: Boolean,
    onInputChange: (String) -> Unit,
    userState: UserState,
    @StringRes labelRes: Int,
    @DrawableRes iconRes: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { onInputChange(it) },
            label = { Text(stringResource(id = labelRes)) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                errorLabelColor = MaterialTheme.colorScheme.onTertiary,
                cursorColor = MaterialTheme.colorScheme.onTertiary,
                errorContainerColor = MaterialTheme.colorScheme.tertiary,
                errorBorderColor = Color.Red,
                errorCursorColor = Color.Red,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = ""
                )
            },
            isError = userState is UserState.Error || isEmptyError && inputText.isEmpty(),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.fillMaxWidth(),
        )
        if (isEmptyError && inputText.isEmpty()) {
            Text(
                text = stringResource(id = R.string.emptyFieldError),
                modifier = Modifier.padding(start = 16.dp)
            )
        } else if (userState is UserState.Error) {
            Text(
                text = userState.message ?: "",
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}