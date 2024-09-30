package com.sotirisapak.apps.pokemonexplorer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sotirisapak.apps.pokemonexplorer.databinding.RvItemPokemonBinding
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.components.BaseAdapter
import com.squareup.picasso.Picasso

/**
 * The adapter to preview [Pokemon] items into a beautiful recycler view list.
 * @param onPokemonClick the callback method to inform the host viewModel about the user's interaction
 * with a specific item in list (selected [Pokemon] item).
 * @author SotirisSapak
 * @since 1.0.0
 */
class PokemonAdapter(
    private val onPokemonClick: (bind: RvItemPokemonBinding, pokemon: Pokemon) -> Unit
): BaseAdapter<RvItemPokemonBinding, Pokemon>(Pokemon.DiffCallback) {

    /**
     * Implement custom view binding
     * @param binding your binding property
     * @param count the number of items in list
     * @param index the current item index
     * @param item the item itself
     * @param onClick the click implementation for your recyclerView item
     * @param onLongClick the long click implementation for your recyclerView item
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun bind(
        binding: RvItemPokemonBinding,
        count: Int,
        index: Int,
        item: Pokemon,
        onClick: (binding: RvItemPokemonBinding, item: Pokemon) -> Unit,
        onLongClick: (binding: RvItemPokemonBinding, item: Pokemon) -> Boolean
    ) {
        binding.pokemon = item
        binding.index = index
        // async load the pokemon icon to imageView
        Picasso
            .get()
            .load(item.sprites.frontDefault)
            .resize(50, 50)
            .onlyScaleDown()
            .into(binding.imagePokemonIcon)
        // attach click listener
        binding.container.setOnClickListener{ onPokemonClick.invoke(binding, item) }
    }

    /**
     * Initialize the DataBinding model.
     *
     * Usage:
     *
     * <code>
     *
     *     // where VB is your ViewBinding type
     *     override fun createBindingImpl(parent: ViewGroup) =
     *          VB.inflate(LayoutInflater.from(parent.context), parent, false)
     * </code>
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun createViewHolder(parent: ViewGroup) =
        RvItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)


}