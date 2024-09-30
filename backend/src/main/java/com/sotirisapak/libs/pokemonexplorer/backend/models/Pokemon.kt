package com.sotirisapak.libs.pokemonexplorer.backend.models

import androidx.recyclerview.widget.DiffUtil
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
    var sprites: Sprites = Sprites()
): Serializable {

    data class Sprites(
        @SerializedName("front_default") var frontDefault: String = ""
    )








    companion object DiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
    }
}