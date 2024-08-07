package de.lucas.cookingrecipes.details.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.lucas.cookingrecipes.recipe.data.Comment
import de.lucas.cookingrecipes.recipe.data.Rating
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.details.domain.RecipeDetailRepository
import timber.log.Timber

class RecipeDetailRepositoryImpl(private val database: FirebaseDatabase) : RecipeDetailRepository {
    override suspend fun getRecipeDetails(key: String, onSuccess: (Recipe) -> Unit) {
        database.getReference("recipes").child(key).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Recipe::class.java)?.let { onSuccess(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("Couldn't load Recipe: ${error.message}")
            }
        })
    }

    override suspend fun onRating(
        userId: String,
        userRatingKey: Int?,
        lastUserRatingKey: Int,
        recipeKey: String,
        recipeId: String,
        userRating: Float,
        ratingSum: Float,
        ratedNumber: Int,
    ) {
        if (userRatingKey != null && userRatingKey != -1) {
            database.getReference("users/${userId}/ratings/${userRatingKey}")
                .setValue(Rating(recipeId, userRating))
            database.getReference("recipes/${recipeKey}/ratingSum")
                .setValue(ratingSum.plus(userRating))
        } else {
            database.getReference("users/${userId}/ratings").child("$lastUserRatingKey")
                .setValue(Rating(recipeId, userRating))
            database.getReference("recipes/${recipeKey}/ratedNumber").setValue(ratedNumber + 1)
            database.getReference("recipes/${recipeKey}/ratingSum")
                .setValue(ratingSum.plus(userRating))
        }
    }

    override suspend fun onSetFavorite(
        userId: String,
        recipeId: String,
        newKey: Int,
        key: Int?,
        isFavorite: Boolean,
    ) {
        if (isFavorite) {
            database.getReference("users/${userId}/favorites").child("$newKey")
                .setValue(recipeId)
        } else {
            database.getReference("users/${userId}/favorites/${key}").removeValue()
        }
    }

    override suspend fun onSendComment(
        key: String,
        user: UserData,
        comment: Comment,
        commentCount: Int,
    ) {
        database.getReference("recipes/${key}/comments/${commentCount}").setValue(comment)
        database.getReference("users/${user.id}/commentCount").setValue(user.commentCount + 1)
    }
}