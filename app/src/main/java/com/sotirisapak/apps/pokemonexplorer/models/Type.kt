package com.sotirisapak.apps.pokemonexplorer.models

import androidx.recyclerview.widget.DiffUtil

/**
 * The pokemon type
 * @param id the identifier of the type as mentioned in PokeAPI
 * @param name the type name
 * @param isSelected option value to be selected or not
 * @author SotirisSapak
 * @since 1.0.0
 */
data class Type(
    var id: Int,
    var name: String,
    var isSelected: Boolean = false
) {
    companion object DiffCallback : DiffUtil.ItemCallback<Type>() {
        override fun areItemsTheSame(oldItem: Type, newItem: Type) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Type, newItem: Type) = oldItem == newItem
    }
}
