package de.lucas.cookingrecipes.profile.domain

import de.lucas.cookingrecipes.auth.data.user.UserData

interface ProfileRepository {
    suspend fun saveUserData(userData: UserData)
}