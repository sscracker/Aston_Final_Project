package org.grigiorev.rickandmortyproject.presentation.episodes.details

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
import org.grigiorev.rickandmortyproject.databinding.FragmentEpisodesDetailsBinding
import org.grigiorev.rickandmortyproject.domain.entities.episode.EpisodeEntity

class EpisodesDetailsFragment : Fragment(R.layout.fragment_episodes_details) {

    private val args by navArgs<EpisodesDetailsFragmentArgs>()

    private val binding by viewBinding(FragmentEpisodesDetailsBinding::bind)
    private val viewModel: EpisodesDetailsGenericViewModel.ViewModel by viewModels<EpisodeDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        viewModel.onViewCreated(args.id)
    }

    private fun initViews() {
        binding.episodeDetailsRetryButton.setOnClickListener { viewModel.onRetryButtonPressed(args.id) }
        binding.episodeDetailsCharactersRecyclerView.adapter = viewModel.adapter
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.renderEpisodeEntityLiveData.observe(viewLifecycleOwner) {
            renderEpisodeEntity(it)
        }
        viewModel.renderCharactersListLiveData.observe(viewLifecycleOwner) {
            viewModel.adapter.updateList(it)
        }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                val direction =
                    EpisodesDetailsFragmentDirections.actionEpisodeDetailsFragmentToCharacterDetailsFragment(
                        it.id, it.name
                    )
                findNavController().navigate(direction)
            }
        }
    }

    private fun renderEpisodeEntity(episodeEntity: EpisodeEntity) {
        val episodeNumberText = "${getString(R.string.episode)}: ${episodeEntity.episode}"
        val airDateText = "${getString(R.string.air_date)}: ${episodeEntity.air_date}"
        binding.episodeDetailsNumberTextView.text = episodeNumberText
        binding.episodeDetailsAirDateTextView.text = airDateText
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.episodeDetailsProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.episodeDetailsRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}