package de.lucas.cookingrecipes.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.lucas.cookingrecipes.R

@Composable
fun EditTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    @StringRes placeHolder: Int,
    isRequired: Boolean = true,
    isNumeric: Boolean = false,
    onValueChanged: (String) -> Unit,
) {
    var inputText by remember { mutableStateOf(initialValue) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val label = stringResource(placeHolder) + if (isRequired) stringResource(R.string.star) else ""
    OutlinedTextField(
        value = inputText,
        onValueChange = {
            inputText = it
            onValueChanged(it)
        },
        placeholder = { Text(text = label, color = Color.Gray) },
        label = { Text(text = label, color = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.White,
            focusedBorderColor = Color.White,
            cursorColor = Color.White,
            unfocusedSuffixColor = Color.White,
            focusedSuffixColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedLabelColor = Color.White,
        ),
        shape = RoundedCornerShape(32.dp),
        maxLines = 3,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = if (isNumeric) KeyboardType.NumberPassword else KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        modifier = modifier,
    )
}