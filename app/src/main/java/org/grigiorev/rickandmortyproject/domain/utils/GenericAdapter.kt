package org.grigiorev.rickandmortyproject.domain.utils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class GenericAdapter<T : IdEntity, E : RecyclerView.ViewHolder>(protected val listener: OnItemClick) :
    RecyclerView.Adapter<E>() {

    protected var entitiesList: MutableList<T> = mutableListOf()

    fun updateList(page: Page<T>) {
        if (page.info.prev == null) {
            renderFirstPage(page)
        } else {
            renderAddedPage(page)
        }
    }

    private fun renderFirstPage(page: Page<T>) {
        val diffUtil = EntitiesDiffUtil(entitiesList, ArrayList(page.results))
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        entitiesList = ArrayList(page.results)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun renderAddedPage(page: Page<T>) {
        if (pageAlreadyAdded(page)) return
        val currentListSize = itemCount
        entitiesList.addAll(page.results)
        notifyItemRangeInserted(currentListSize, page.results.size)
    }

    private fun pageAlreadyAdded(page: Page<T>): Boolean =
        page.results.isEmpty() || entitiesList.last().id == page.results.last().id


    override fun getItemCount(): Int = entitiesList.size

    private class EntitiesDiffUtil<K : IdEntity>(
        private val oldList: MutableList<K>,
        private val newList: MutableList<K>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

}