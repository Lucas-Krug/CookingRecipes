package de.lucas.cookingrecipes.auth.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.lucas.cookingrecipes.core.data.db.Preferences
import de.lucas.cookingrecipes.auth.domain.user.UserRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val preferences: Preferences,
) : UserRepository {

    override suspend fun getUserFromDb(): UserData? = preferences.getUserData()

    override suspend fun loginUser(
        email: String,
        password: String,
        onSuccess: (UserData) -> Unit,
        onError: () -> Unit,
    ) {
        try {
            val result = auth.signInWithEmailAndPassword(email.trim(), password.trim()).await()
            val user = result.user
            if (user != null) {
                setUserDataListener(userId = user.uid, onSuccess = onSuccess, onError = onError)
            } else {
                onError()
            }
        } catch (e: Exception) {
            Timber.e("Sign in failed", e)
            onError()
        }
    }

    override suspend fun signUpUser(
        email: String,
        password: String,
        username: String,
        onSuccess: (UserData) -> Unit,
        onError: () -> Unit,
    ) {
        try {
            val result = auth.createUserWithEmailAndPassword(email.trim(), password.trim()).await()
            val user = result.user
            if (user != null) {
                val userData = UserData(
                    id = user.uid,
                    name = username.trim(),
                    email = email.trim(),
                    picture = "",
                    myRecipes = listOf(),
                    commentCount = 0,
                )
                database.reference.child("User/${user.uid}").setValue(userData)
                setUserDataListener(userId = user.uid, onSuccess = onSuccess, onError = onError)
            } else {
                onError()
            }
        } catch (e: Exception) {
            Timber.e("Registration failed", e)
            onError()
        }
    }

    override fun setUserDataListener(
        userId: String,
        onSuccess: (UserData) -> Unit,
        onError: () -> Unit,
    ) {
        database.getReference("users/${userId}").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    snapshot.getValue(UserData::class.java)?.let {
                        onSuccess(it.copy(id = userId))
                        preferences.setUserData(it.copy(id = userId))
                    }
                } catch (e: Exception) {
                    if (e is DatabaseException) {
                        database.getReference("users/${userId}/favorites").removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("Couldn't load Recipe: ${error.message}")
                onError()
            }
        })
    }

    override suspend fun logoutUser() {
        auth.signOut()
        preferences.clearUserData()
    }

    override fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.e("Email sent.")
                }
            }
    }
}