package de.lucas.cookingrecipes.core.presentation.util

fun String.isValidEmail(): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
    return this.matches(emailPattern.toRegex())
}