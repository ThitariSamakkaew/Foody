package com.thitari.foody.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thitari.foody.models.FoodRecipe
import com.thitari.foody.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipes: FoodRecipe) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}