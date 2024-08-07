package de.lucas.cookingrecipes.core.data.mock

import de.lucas.cookingrecipes.recipe.data.Comment
import de.lucas.cookingrecipes.recipe.data.Difficulty
import de.lucas.cookingrecipes.recipe.data.Rating
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.data.user.UserData

class Mock {
    val recipe = Recipe(
        id = "1",
        name = "Lentil soup",
        description = "This mouth-watering recipe is ready in just 1 hour and 10 minutes and the ingredients detailed below can serve up to 6 people.",
        difficulty = Difficulty.VERY_EASY.value,
        portions = 6,
        time = 70,
        tags = listOf(
            "olive",
            "onion",
            "carrot",
            "celery",
            "garlic",
            "pepper",
            "tomato",
            "lentil",
            "chicken",
            "thyme",
            "macaroni",
            "parmesan",
            "Lunch",
            "Starters",
            "Healthy",
            "Italian",
        ),
        ingredients = listOf(
            "60ml olive oil, plus extra for drizzling",
            "60ml olive oil, plus extra for drizzling",
            "1 medium onion, chopped",
            "2 carrots, peeled and chopped",
            "2 celery stalks, chopped",
            "2 garlic cloves, chopped",
            "Salt and freshly ground black pepper",
            "1 (400g) tin chopped tomatoes",
            "450g lentils",
            "2.5L chicken stock",
            "4 to 6 fresh thyme sprigs",
            "150g shredded Parmesan",
        ),
        directions = listOf(
            "Heat the oil in a heavy large pot over medium heat. Add the onion, carrots, and celery. Add the garlic, salt, and pepper and saute until all the vegetables are tender, about five to eight minutes. Add the tomatoes with their juices. Simmer until the juices evaporate a little and the tomatoes break down, stirring occasionally, about eight minutes. Add the lentils and mix to coat. Add the broth and stir. Add the thyme sprigs. Bring to a boil over high heat. Cover and simmer over low heat until the lentils are almost tender, about 30 minutes.",
            "Stir in the pasta. Simmer until the pasta is tender but still firm to the bite, about eight minutes. Season with salt and pepper, to taste.",
            "Ladle the soup into bowls. Sprinkle with the Parmesan, drizzle with olive oil, and serve.",
        ),
        calories = 349,
        carbs = 48,
        fat = 10,
        protein = 18,
        ratingSum = 212.0f,
        ratedNumber = 41,
        thumbnail = "https://d2vsf1hynzxim7.cloudfront.net/production/media/23441/responsive-images/8489894_FNK_the-best-lentil-soup_H___default_2074_1556.webp",
        top10 = 0,
        videolink = "https://www.youtube.com/watch?v=ZKiDyHjdPl8",
        authorName = "Berta Bertosch",
    )

    val recipeList = listOf(
        recipe,
        recipe,
        recipe,
        recipe,
        recipe,
    )

    val tagList: Map<String, List<Recipe>> = mapOf("German" to recipeList)

    val tag = "Soup"

    val ratings = listOf(Rating("2", 4.0f))

    val comments = listOf(
        Comment(
            username = "Max Mustermann",
            picture = "",
            timestamp = (System.currentTimeMillis().minus(2000.toLong())),
            comment = "Very nice recipe!"
        ),
        Comment(
            username = "Berta Bertosch",
            picture = "",
            timestamp = 1720303200000,
            comment = "I tried it and loved it."
        ),
        Comment(
            username = "Niko Dinkel",
            picture = "",
            timestamp = 1717711200000,
            comment = "Pretty easy to make. I used a few different ingredients and it was still great. Next time I will try it with all the right ingredients. It will probably be even better!"
        ),
    )

    val userData = UserData(
        id = "1",
        name = "Berta Bertosch",
        picture = "",
        email = "berta_bertosch@gmail.com",
        myRecipes = listOf(recipe.id, recipe.id),
        commentCount = 4,
    )
}
