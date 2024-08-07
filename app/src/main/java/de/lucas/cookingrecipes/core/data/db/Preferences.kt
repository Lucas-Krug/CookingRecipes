package de.lucas.cookingrecipes.core.data.db

import android.content.Context
import de.lucas.cookingrecipes.auth.data.user.UserData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Preferences(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun setUserData(user: UserData) {
        val editor = sharedPreferences.edit()
        editor.putString(USER, Json.encodeToString(user)).apply()
    }

    fun getUserData() = sharedPreferences.getString(USER, null)
        ?.let { Json.decodeFromString<UserData>(it) }

    fun clearUserData() = sharedPreferences.edit().remove(USER).apply()

    companion object {
        const val USER = "USER"
    }
}