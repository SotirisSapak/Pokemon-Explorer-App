package com.sotirisapak.libs.pokemonexplorer.backend.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * The actual pokemon model to provide all information about a specific pokemon. This class could
 * also be saved locally via room as [Entity]. With serialization, we can also pass this object as parameter
 * to another activity or fragment...
 * @param id the id of the pokemon and the primary key of the room database entity
 * @param name the pokemon name
 * @param weight the weight of this Pokémon in hectograms.
 * @param height the height of this Pokémon in decimetres.
 * @param sprites the images of this Pokemon as sprites (url to fetch)
 * @param stats the base stats of this Pokemon
 * @see <a href="https://pokeapi.co/docs/v2#pokemon">Pokemon api reference</a>
 * @author SotirisSapak
 * @since 0.0.1
 */
@Entity
data class Pokemon(
    @PrimaryKey var id: Int = -1,
    var name: String = "",
    var weight: Int = 0,
    var height: Int = 0,
    @Embedded var sprites: Sprites = Sprites(),
    var stats: List<Stats> = emptyList()
): Serializable {

    /**
     * The images of the pokemon
     * @param frontDefault the default image of the pokemon
     * @author SotirisSapak
     * @since 1.0.0
     */
    data class Sprites(@SerializedName("front_default") var frontDefault: String = ""): Serializable

    /**
     * The [DiffUtil.ItemCallback] of [Pokemon] component to be used in any List adapter.
     * @author SotirisSapak
     * @since 1.0.0
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
    }

    /**
     * The base stats of the pokemon as reference model
     * @param baseStat the value of the stat
     * @param effort the effort property
     * @param stat a component to fetch the [Stat.statName] of the stat. New class due to api response
     * style
     * @author SotirisSapak
     * @since 1.0.0
     */
    data class Stats(
        @SerializedName("base_stat") var baseStat: Int = 0,
        var effort: Int = 0,
        var stat: Stat = Stat()
    ): Serializable {

        /**
         * The stat information
         * @param statName the name of the stat
         * @author SotirisSapak
         * @since 1.0.0
         */
        data class Stat(@SerializedName("name") var statName: String = ""): Serializable

    }

}


