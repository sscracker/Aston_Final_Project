package org.grigiorev.rickandmortyproject.presentation.episodes

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.grigiorev.rickandmortyproject.R
import org.grigiorev.rickandmortyproject.databinding.FragmentEpisodesBinding
import org.grigiorev.rickandmortyproject.domain.utils.IdEntity

class EpisodesFragment : Fragment(R.layout.fragment_episodes) {

    private val binding by viewBinding(FragmentEpisodesBinding::bind)
    private val viewModel: EpisodesGenericViewModel.ViewModel by viewModels<EpisodesViewModel>()

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
        binding.episodesRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            binding.episodesRefreshLayout.isRefreshing = false
        }
    }


    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.renderEpisodesListLiveData.observe(viewLifecycleOwner) { adapter.updateList(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.emptyResponseLiveData.observe(viewLifecycleOwner) { setEmptyResponseMode(it) }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { openEpisodeDetails(it) }
        }
    }

    private fun openEpisodeDetails(entity: IdEntity) {
        val direction = EpisodesFragmentDirections.actionEpisodesFragmentToEpisodeDetailsFragment(
            entity.id,
            entity.name
        )
        findNavController().navigate(direction)
    }

    private fun setEmptyResponseMode(isVisible: Boolean) {
        binding.episodesEmptyResponseTextView.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.episodesRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.episodesProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.episodesRecyclerView.adapter = adapter
        binding.episodesRecyclerView.addOnScrollListener(object :
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
        binding.episodesRetryButton.setOnClickListener { viewModel.onRetryButtonPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //do nothing
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onQueryTextChange(newText)
                return true
            }

        })
    }

    private fun hideKeyBoard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}