package de.lucas.cookingrecipes.profile.data

import com.google.firebase.database.FirebaseDatabase
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.core.data.db.Preferences
import de.lucas.cookingrecipes.profile.domain.ProfileRepository
import timber.log.Timber

class ProfileRepositoryImpl(
    private val database: FirebaseDatabase,
    private val preferences: Preferences,
) : ProfileRepository {
    override suspend fun saveUserData(userData: UserData) {
        Timber.e(userData.toString())
        database.getReference("users/${preferences.getUserData()?.id}").setValue(userData)
        preferences.setUserData(userData)
    }
}