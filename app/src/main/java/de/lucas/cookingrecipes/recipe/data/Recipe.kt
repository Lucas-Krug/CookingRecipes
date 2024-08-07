package de.lucas.cookingrecipes.recipe.data

import de.lucas.cookingrecipes.core.data.serialization.UrlEncoder
import de.lucas.cookingrecipes.core.data.serialization.UrlListEncoder
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val key: String? = null,
    val id: String = "-1",
    val authorName: String = "",
    val name: String = "",
    val description: String = "",
    val difficulty: String = "",
    val portions: Int = 0,
    val time: Int = 0,
    @Serializable(with = UrlListEncoder::class)
    val ingredients: List<String> = emptyList(),
    @Serializable(with = UrlListEncoder::class)
    val directions: List<String> = emptyList(),
    val calories: Int? = null,
    val fat: Int? = null,
    val carbs: Int? = null,
    val protein: Int? = null,
    val tags: List<String> = emptyList(),
    val ratedNumber: Int = 0,
    val ratingSum: Float = 0f,
    @Serializable(with = UrlEncoder::class)
    val thumbnail: String = "",
    val top10: Int = 0,
    @Serializable(with = UrlEncoder::class)
    val videolink: String = "",
    val comments: List<Comment>? = emptyList(),
)

enum class Difficulty(val value: String) {
    VERY_EASY("Very Easy"),
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    VERY_HARD("Very Hard"),
}
