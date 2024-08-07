package de.lucas.cookingrecipes.details.presentation.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.core.data.mock.Mock
import de.lucas.cookingrecipes.recipe.data.Comment
import de.lucas.cookingrecipes.core.presentation.components.DebugPlaceholderCircle
import de.lucas.cookingrecipes.main.theme.CookingRecipesTheme
import de.lucas.cookingrecipes.core.presentation.util.toTimeAgo

@Composable
fun CommentSection(
    comments: List<Comment>,
    isLoggedIn: Boolean,
    onComment: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Text(
            text = stringResource(R.string.comments, comments.size),
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CommentTextField(isLoggedIn = isLoggedIn, sendComment = { comment -> onComment(comment) })
        if (comments.isEmpty()) {
            Text(
                text = stringResource(R.string.noComments),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
            )
        } else {
            comments.sortedByDescending { it.timestamp }.forEach { comment ->
                Comment(comment = comment)
            }
        }
    }
}

@Composable
private fun CommentTextField(isLoggedIn: Boolean, sendComment: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf("") }

    Row {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text(stringResource(R.string.commentHere), color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                cursorColor = Color.White,
            ),
            shape = RoundedCornerShape(32.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (inputText.isNotEmpty()) {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        sendComment(inputText.trim())
                        if (isLoggedIn) {
                            inputText = ""
                        }
                    }
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        )
        Button(
            enabled = inputText.isNotEmpty(),
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                sendComment(inputText.trim())
                if (isLoggedIn) {
                    inputText = ""
                }
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = "send",
            )
        }
    }
}

@Composable
private fun Comment(comment: Comment) {
    Column(Modifier.padding(top = 24.dp, bottom = 16.dp)) {
        Row {
            SubcomposeAsyncImage(
                model = comment.picture,
                contentDescription = comment.comment,
                loading = {
                    DebugPlaceholderCircle(R.drawable.ic_woman_profile)
                },
                contentScale = ContentScale.FillBounds,
                error = { painterResource(R.drawable.profile_default) },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.padding(end = 8.dp))
            Column {
                Row(Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = comment.username,
                        color = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = comment.timestamp.toTimeAgo(), color = Color.Gray)
                }
                Text(text = comment.comment, color = Color.White)
            }
        }
    }
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
}

@Preview
@Composable
private fun PreviewCommentSectionNoComments() {
    CookingRecipesTheme {
        CommentSection(comments = listOf(), isLoggedIn = true, onComment = {})
    }
}

@Preview
@Composable
private fun PreviewCommentSectionComments() {
    CookingRecipesTheme {
        CommentSection(comments = Mock().comments, isLoggedIn = true, onComment = {})
    }
}