package de.lucas.cookingrecipes.auth.domain.user

import de.lucas.cookingrecipes.auth.data.user.UserData

interface UserRepository {

    suspend fun getUserFromDb(): UserData?

    suspend fun loginUser(
        email: String,
        password: String,
        onSuccess: (UserData) -> Unit,
        onError: () -> Unit,
    )

    suspend fun signUpUser(
        email: String,
        password: String,
        username: String,
        onSuccess: (UserData) -> Unit,
        onError: () -> Unit,
    )

    suspend fun logoutUser()

    fun setUserDataListener(
        userId: String,
        onSuccess: (UserData) -> Unit,
        onError: () -> Unit,
    )

    fun resetPassword(email: String)
}