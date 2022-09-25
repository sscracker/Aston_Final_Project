package org.grigiorev.rickandmortyproject.presentation.locations

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.grigiorev.rickandmortyproject.R
import org.grigiorev.rickandmortyproject.databinding.FragmentLocationsBinding
import org.grigiorev.rickandmortyproject.domain.utils.IdEntity

class LocationsFragment : Fragment(R.layout.fragment_locations) {

    private val binding by viewBinding(FragmentLocationsBinding::bind)
    private val viewModel: LocationsGenericViewModel.ViewModel by viewModels<LocationsViewModel>()

    private val adapter by lazy { viewModel.adapter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        initRecyclerView()
        initRetryButton()
        binding.locationsRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            binding.locationsRefreshLayout.isRefreshing = false
        }
    }


    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.renderLocationsListLiveData.observe(viewLifecycleOwner) { adapter.updateList(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.emptyResponseLiveData.observe(viewLifecycleOwner) { setEmptyResponseMode(it) }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { openLocationDetails(it) }
        }
    }

    private fun openLocationDetails(entity: IdEntity) {
        val direction =
            LocationsFragmentDirections.actionLocationsFragmentToLocationDetailsFragment(
                entity.id,
                entity.name
            )
        findNavController().navigate(direction)
    }

    private fun setEmptyResponseMode(isVisible: Boolean) {
        binding.locationsEmptyResponseTextView.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.locationsRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.locationsProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.locationsRecyclerView.adapter = adapter
        binding.locationsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.onRecyclerViewScrolledDown()
                }
            }
        })
    }

    private fun initRetryButton() {
        binding.locationsRetryButton.setOnClickListener { viewModel.onRetryButtonPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onQueryTextChange(newText)
                return true
            }

        })

    }

}