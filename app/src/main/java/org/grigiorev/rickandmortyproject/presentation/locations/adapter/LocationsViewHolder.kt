package org.grigiorev.rickandmortyproject.presentation.locations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.grigiorev.rickandmortyproject.databinding.ItemLocationBinding
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.utils.OnItemClick

class LocationsViewHolder(parent: ViewGroup, listener: OnItemClick) :
    RecyclerView.ViewHolder(
        ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {

    init {
        itemView.setOnClickListener { listener.onClick(currentLocation) }
    }

    private val binding = ItemLocationBinding.bind(itemView)
    private lateinit var currentLocation: LocationEntity

    fun bind(locationEntity: LocationEntity) {
        currentLocation = locationEntity
        binding.itemLocationNameTextView.text = locationEntity.name
        binding.itemLocationTypeTextView.text = locationEntity.type
        binding.itemLocationDimensionTextView.text = locationEntity.dimension
    }
}