package com.thitari.foody.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thitari.foody.models.FoodJoke
import com.thitari.foody.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(@Embedded var foodJoke: FoodJoke) {

    @PrimaryKey(autoGenerate = false) // false because we have 1 role and update any time when we request the new data from api
    var id: Int = 0
}