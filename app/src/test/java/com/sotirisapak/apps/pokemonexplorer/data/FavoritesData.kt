package com.sotirisapak.apps.pokemonexplorer.data

import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * Dataset for favorites list
 */
object FavoritesData {

    val pokemon1 = Pokemon(
        id = 1,
        name = "pokemon1",
        weight = 10,
        height = 20,
        stats = listOf(
            Pokemon.Stats(baseStat = 10, stat = Pokemon.Stats.Stat(statName = "hp"))
        )
    )
    val pokemon2 = Pokemon(
        id = 2,
        name = "pokemon2",
        weight = 50,
        height = 100,
        stats = listOf(
            Pokemon.Stats(baseStat = 60, stat = Pokemon.Stats.Stat(statName = "hp"))
        )
    )
    val pokemon3 = Pokemon(
        id = 1,
        name = "pokemon3",
        weight = 200,
        height = 380,
        stats = listOf(
            Pokemon.Stats(baseStat = 30, stat = Pokemon.Stats.Stat(statName = "hp"))
        )
    )

    private val emptyList = mutableListOf<Pokemon>()
    private val listWithOnlyPokemon1 = mutableListOf(pokemon1)
    private val listWithAllPokemon = mutableListOf(pokemon1, pokemon2, pokemon3)

}