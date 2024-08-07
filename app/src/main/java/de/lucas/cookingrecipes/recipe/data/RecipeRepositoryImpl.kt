package de.lucas.cookingrecipes.recipe.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.lucas.cookingrecipes.recipe.domain.RecipeRepository
import timber.log.Timber

class RecipeRepositoryImpl(private val database: FirebaseDatabase) : RecipeRepository {

    override suspend fun getRecipes(
        onSuccess: (List<Recipe>) -> Unit,
        onError: () -> Unit,
    ) {
        database.getReference("recipes").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipes = mutableListOf<Recipe>()
                snapshot.children.forEach {
                    it.getValue(Recipe::class.java)?.let { recipe ->
                        recipes.add(recipe.copy(key = it.key))
                    }
                }
                onSuccess(recipes)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("Couldn't load Recipes: ${error.message}")
                onError()
            }
        })
    }
}