package de.lucas.cookingrecipes.create.data

import com.google.firebase.database.FirebaseDatabase
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.create.domain.CreateRecipeRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CreateRecipeRepositoryImpl(private val database: FirebaseDatabase) : CreateRecipeRepository {
    override suspend fun createRecipe(userData: UserData, recipe: Recipe): String {
        val keyRef = database.getReference("recipes").orderByKey().limitToLast(1).get().await()
        val newKey = keyRef.children.firstOrNull()?.key?.toInt()?.plus(1).toString()
        database.getReference("recipes/$newKey")
            .setValue(recipe.copy(id = UUID.randomUUID().toString()))
        return newKey
    }
}