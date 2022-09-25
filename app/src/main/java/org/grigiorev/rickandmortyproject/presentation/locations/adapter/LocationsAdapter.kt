package org.grigiorev.rickandmortyproject.presentation.locations.adapter

import android.view.ViewGroup
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity
import org.grigiorev.rickandmortyproject.domain.utils.GenericAdapter
import org.grigiorev.rickandmortyproject.domain.utils.OnItemClick

class LocationsAdapter(listener: OnItemClick) :
    GenericAdapter<LocationEntity, LocationsViewHolder>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}