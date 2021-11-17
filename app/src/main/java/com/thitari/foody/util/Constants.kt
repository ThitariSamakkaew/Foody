package com.thitari.foody.util

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "100c2a9578e843189f10389cd82348d8"
        const val RECIPE_RESULT_KEY = "recipeBundle"

        // Api Queries key
        const val QUERIES_NUMBER = "number"
        const val QUERIES_API_KEY = "apiKey"
        const val QUERIES_TYPE = "type"
        const val QUERIES_DIET = "diet"
        const val QUERIES_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERIES_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_SEARCH = "query"

        // Data base
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"

        // Bottom sheet and Preferences
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_RECIPES_NUMBER = "10"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCE_MEAL_TYPE = "mealType"
        const val PREFERENCE_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCE_DIET_TYPE = "dietType"
        const val PREFERENCE_DIET_TYPE_ID = "dietTypeId"

        //BackOnline
        const val PREFERENCES_BACK_ONLINE = "backOnline"
    }
}