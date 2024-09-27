package com.sotirisapak.apps.pokemonexplorer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.sotirisapak.apps.pokemonexplorer.databinding.RvItemTypeBinding
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.libs.pokemonexplorer.core.components.BaseAdapter

/**
 * The adapter for the recycler view that holds the pokemon types
 * @author SotirisSapak
 * @since 1.0.0
 */
class TypeAdapter(
    private val onTypeSelection: (bind: RvItemTypeBinding, selectedType: Type) -> Unit = { _, _ -> }
): BaseAdapter<RvItemTypeBinding, Type>(
    diffUtil = Type.DiffCallback,
    onClick = onTypeSelection
) {

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
    @SuppressLint("NotifyDataSetChanged")
    override fun bind(
        binding: RvItemTypeBinding,
        count: Int,
        index: Int,
        item: Type,
        onClick: (binding: RvItemTypeBinding, item: Type) -> Unit,
        onLongClick: (binding: RvItemTypeBinding, item: Type) -> Boolean
    ) {
        binding.type = item
        onOptionSelectionLayoutHandler(binding, item)
        // assign click listener to card
        binding.cardType.setOnClickListener { onItemClick(binding, item) }
    }

    /**
     * View related method to adjust the layout state bases on if current item is selected or not.
     * We do this in order to create a selectable item list
     * @param binding the item binding property
     * @param item the current binding item to  object check if is selected or not. [Type.isSelected]
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun onOptionSelectionLayoutHandler(binding: RvItemTypeBinding, item: Type) {
        // when a type is selected, the application should invert the colors of the card and text
        // accordingly.
        if(item.isSelected) {
            binding.imageSelectionGradient.alpha = 0.6f
            binding.textType.typeface = ResourcesCompat.getFont(
                binding.root.context,
                com.sotirisapak.libs.pokemonexplorer.framework.R.font.font_bold
            )
        } else {
            binding.imageSelectionGradient.alpha = 0f
            binding.textType.typeface = ResourcesCompat.getFont(
                binding.root.context,
                com.sotirisapak.libs.pokemonexplorer.framework.R.font.font_regular
            )
        }
    }

    /**
     * Some actions are been invoked when item is click so i decided to group these commands into a
     * single method!
     * @param binding the item binding property
     * @param item current binding item object
     * @author SotirisSapak
     * @since 1.0.0
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun onItemClick(binding: RvItemTypeBinding, item: Type) {
        // when this card clicked, then assign all items the isEnabled property to false, and this
        // item to true
        for(listItem in this.currentList) listItem.isSelected = false
        item.isSelected = true
        onTypeSelection.invoke(binding, item)
        notifyDataSetChanged()
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
        RvItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

}