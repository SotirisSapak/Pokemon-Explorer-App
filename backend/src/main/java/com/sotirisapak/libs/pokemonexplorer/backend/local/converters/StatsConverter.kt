package com.sotirisapak.libs.pokemonexplorer.backend.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sotirisapak.libs.pokemonexplorer.backend.extensions.fromJson
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * Implement converter to provide a proper way for room database to manipulate a list of [Pokemon.Stats]
 * objects.
 * @author SotirisSapak
 * @since 1.0.0
 */
class StatsConverter {


    /**
     * Type converter to convert a [stats] json string to [List] of [Pokemon.Stats] components
     * @param stats the json that contain the stats
     * @return a new [List] of [Pokemon.Stats] instances based on given [stats] json. If any exception
     * occurs, then this will return an empty list
     * @author SotirisSapak
     * @since 1.0.0
     */
    @TypeConverter
    fun toStatsList(stats: String): List<Pokemon.Stats> {
        return try {
            Gson().fromJson<List<Pokemon.Stats>>(stats)
        } catch (ex: Exception) {
            emptyList()
        }
    }

    /**
     * Type converter to convert a [List] of [Pokemon.Stats] components to proper json string to be
     * saved in room database.
     * @param stats the [List] of [Pokemon.Stats] to be converted to json
     * @return a new json string based on given [List] of [Pokemon.Stats]. If any exception occurs,
     * then this will return an empty string
     * @author SotirisSapak
     * @since 1.0.0
     */
    @TypeConverter
    fun toStatsListJson(stats: List<Pokemon.Stats>): String {
        return try {
            Gson().toJson(stats)
        } catch (ex: Exception) {
            ""
        }
    }
    
}