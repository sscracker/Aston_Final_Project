package org.grigiorev.rickandmortyproject.presentation.locations.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import org.grigiorev.rickandmortyproject.R
import org.grigiorev.rickandmortyproject.databinding.FragmentLocationsDetailsBinding
import org.grigiorev.rickandmortyproject.domain.entities.location.LocationEntity

class LocationsDetailsFragment : Fragment(R.layout.fragment_locations_details) {

    private val args by navArgs<LocationsDetailsFragmentArgs>()

    private val binding by viewBinding(FragmentLocationsDetailsBinding::bind)
    private val viewModel: LocationsDetailsGenericViewModel.ViewModel by viewModels<LocationDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        viewModel.onViewCreated(args.id)
    }

    private fun initViews() {
        binding.locationDetailsRetryButton.setOnClickListener { viewModel.onRetryButtonPressed(args.id) }
        binding.locationDetailsCharactersRecyclerView.adapter = viewModel.adapter
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.renderLocationEntityLiveData.observe(viewLifecycleOwner) {
            renderLocationEntity(it)
        }
        viewModel.renderCharactersListLiveData.observe(viewLifecycleOwner) {
            viewModel.adapter.updateList(it)
        }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                val direction =
                    LocationsDetailsFragmentDirections.actionLocationDetailsFragmentToCharacterDetailsFragment(
                        it.id, it.name
                    )
                findNavController().navigate(direction)
            }
        }
    }

    private fun renderLocationEntity(locationEntity: LocationEntity) {
        val typeText = "${getString(R.string.type)}: ${locationEntity.type}"
        val dimensionText = "${getString(R.string.dimension)}: ${locationEntity.dimension}"
        binding.locationDetailsTypeTextView.text = typeText
        binding.locationDetailsDimensionTextView.text = dimensionText
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.locationDetailsProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.locationDetailsRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}