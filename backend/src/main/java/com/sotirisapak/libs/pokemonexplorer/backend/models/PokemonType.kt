package com.sotirisapak.libs.pokemonexplorer.backend.models

import com.google.gson.annotations.SerializedName

/**
 * The pokemon type model
 * @param name the type name as fetched from api
 * @param pokemonList the [TypePokemon] to preview all pokemon related to this [PokemonType]
 * @author SotirisSapak
 * @since 1.0.0
 */
data class PokemonType(
    var name: String = "",
    @SerializedName("pokemon") val pokemonList: List<TypePokemon> = emptyList()
) {
    /**
     * Important class for parsing. This class will hold the pokemon standard information as fetched
     * from type endpoint
     * @param name the pokemon name
     * @param url the url to use in order to fetch all information about this pokemon
     * @author SotirisSapak
     * @since 1.0.0
     */
    data class TypePokemon(
        @SerializedName("pokemon") val pokemonInformation: PokemonInformation = PokemonInformation(),
        var slot: Int = 0
    ) {

        data class PokemonInformation(
            var name: String = "",
            var url: String = ""
        )

    }



}


