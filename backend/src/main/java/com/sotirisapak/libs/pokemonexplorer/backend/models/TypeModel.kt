package com.sotirisapak.libs.pokemonexplorer.backend.models

data class Pokemon(
    val name: String,
    val url: String
)
data class Slot(val slot: Int)

data class PokemonComponent(
    val pokemon: Pokemon,
    val slot: Int
)

data class TypeModel(
    val name: String,
    val pokemon: List<PokemonComponent>
)
