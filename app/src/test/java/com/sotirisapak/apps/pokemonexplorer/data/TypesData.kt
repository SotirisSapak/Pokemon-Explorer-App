package com.sotirisapak.apps.pokemonexplorer.data

import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType

/**
 * Dataset for types
 */
object TypesData {

    val typeSteel = Type(id = 9, name = "Steel")
    val typeSteelSelected = Type(id = 9, name = "Steel", isSelected = true)

    val typeFire = Type(id = 10, name = "Fire")
    val typeFireSelected = Type(id = 10, name = "Fire", isSelected = true)

    val fetchedTypeSteel = PokemonType(name = "Steel", pokemonList = listOf(
            PokemonType.TypePokemon(
                pokemonInformation = PokemonType.TypePokemon.PokemonInformation(
                    name = "pokemon1",
                    url = "pokemon1 url"
                ),
                slot = 1
            ),
            PokemonType.TypePokemon(
                pokemonInformation = PokemonType.TypePokemon.PokemonInformation(
                    name = "pokemon2",
                    url = "pokemon2 url"
                ),
                slot = 2
            )
        ))
    val fetchedTypeFire = PokemonType(name = "Fire", pokemonList = listOf(
        PokemonType.TypePokemon(
            pokemonInformation = PokemonType.TypePokemon.PokemonInformation(
                name = "pokemon3",
                url = "pokemon3 url"
            ),
            slot = 1
        ),
        PokemonType.TypePokemon(
            pokemonInformation = PokemonType.TypePokemon.PokemonInformation(
                name = "pokemon4",
                url = "pokemon4 url"
            ),
            slot = 2
        )
    ))


    val typeUnknown = Type(-1, name = "Unknown")


}