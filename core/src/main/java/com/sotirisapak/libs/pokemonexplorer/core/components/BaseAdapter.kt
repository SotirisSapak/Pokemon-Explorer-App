package com.sotirisapak.libs.pokemonexplorer.core.components

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Custom adapter implementation will no longer use callback for item interaction and you can call
 * item interaction as function method. Will also include binding property for shared element
 * transitions between recyclerView item and next fragment or activity.
 *
 * In order to successfully implement the interactions, you need to configure the [onClick] and [onLongClick]
 * interactions in [bind] method.
 *
 * For example, if you have a "content" layout that need click interaction you have to set:
 *
 * <code>
 *
 *     // at <bind> method:
 *     binding.content.setOnClickListener { onClick.invoke() }
 * </code>
 * @param diffUtil the diffUtil callback interface for the [T] item
 * @param onClick on click function implementation for recyclerView component
 * @param onLongClick on long click function implementation for recyclerView component
 * @author SotirisSapak
 * @since 1.0.0
 */
abstract class BaseAdapter<VB: ViewBinding, T: Any>(
    private val diffUtil: DiffUtil.ItemCallback<T>,
    open val onClick: (binding: VB, item: T) -> Unit = { _, _ -> /* empty call */ },
    open val onLongClick: (binding: VB, item: T) -> Boolean = { _, _ -> false }
): ListAdapter<T, RecyclerView.ViewHolder>(diffUtil) {

    /**
     * ViewHolder implementation for quick viewHolder initialization
     * @param binding the binding instance
     * @author SotirisSapak
     * @since 0.0.3
     */
    inner class ViewHolder(private val binding: VB): RecyclerView.ViewHolder(binding.root){

        /**
         * Attach all binding properties via abstract method
         * @param count the number of recyclerView items
         * @param index the current binding item index
         * @param item the actual item instance of [T]
         * @param onClick the click implementation
         * @param onLongClick the long click implementation
         * @author SotirisSapak
         * @since 1.0.0
         */
        fun attach(
            count: Int,
            index: Int,
            item: T,
            onClick: (binding: VB, item: T) -> Unit,
            onLongClick: (binding: VB, item: T) -> Boolean
        ) = bind(binding, count, index, item, onClick, onLongClick)

    }

    /**
     * Custom implementation to submit a new list to adapter
     * @param list the new list to add
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun submitList(list: List<T>?) = super.submitList(list?.let { ArrayList(it) })

    /**
     * Create viewHolder implementation based on viewType (current we have only one).
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(createViewHolder(parent))

    /**
     * Override [getItemViewType] to avoid view recycling
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun getItemViewType(position: Int) = position

    /**
     * Bind the different type of views (only one)
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        @Suppress("UNCHECKED_CAST")
        val mHolder = holder as BaseAdapter<VB, T>.ViewHolder
        mHolder.apply {
            attach(
                count = itemCount,
                index = position,
                item = getItem(position),
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }


    // ? ===========================================================================================
    // ? Abstract methods to implement
    // ? ===========================================================================================

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
    abstract fun bind(
        binding: VB,
        count: Int,
        index: Int,
        item: T,
        onClick: (binding: VB, item: T) -> Unit,
        onLongClick: (binding: VB, item: T) -> Boolean
    )

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
    abstract fun createViewHolder(parent: ViewGroup): VB

}