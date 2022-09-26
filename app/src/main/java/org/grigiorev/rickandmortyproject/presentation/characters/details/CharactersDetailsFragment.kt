package org.grigiorev.rickandmortyproject.presentation.characters.details

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import org.grigiorev.rickandmortyproject.R
import org.grigiorev.rickandmortyproject.databinding.FragmentCharactersDetailsBinding
import org.grigiorev.rickandmortyproject.domain.entities.character.CharacterEntity

class CharacterDetailsFragment : Fragment(R.layout.fragment_characters_details) {

    private val args by navArgs<CharacterDetailsFragmentArgs>()

    private val binding by viewBinding(FragmentCharactersDetailsBinding::bind)
    private val viewModel: CharactersDetailsGenericViewModel.ViewModel by viewModels<CharactersDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        viewModel.onViewCreated(args.id)
    }

    private fun initViews() {
        binding.characterDetailsRetryButton.setOnClickListener { viewModel.onRetryButtonPressed(args.id) }
        binding.characterDetailsEpisodesRecyclerView.adapter = viewModel.adapter
        binding.characterDetailsOriginTextView.setOnClickListener { viewModel.onOriginLinkPressed() }
        binding.characterDetailsLocationTextView.setOnClickListener { viewModel.onLocationLinkPressed() }
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.renderCharacterEntityLiveData.observe(viewLifecycleOwner) {
            renderCharacterEntity(it)
        }
        viewModel.renderEpisodesListLiveData.observe(viewLifecycleOwner) {
            viewModel.adapter.updateList(it)
        }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                val direction =
                    CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToEpisodeDetailsFragment(
                        it.id, it.name
                    )
                findNavController().navigate(direction)
            }
        }
        viewModel.openLocationDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                val direction =
                    CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToLocationDetailsFragment(
                        it.id, it.name
                    )
                findNavController().navigate(direction)
            }
        }
    }

    private fun renderCharacterEntity(characterEntity: CharacterEntity) {
        Glide
            .with(requireContext())
            .load(characterEntity.image)
            .into(binding.characterDetailsImageView)
        val statusText = "${getString(R.string.status)}: ${characterEntity.status}"
        val speciesText = "${getString(R.string.species)}: ${characterEntity.species}"
        val typeText = "${getString(R.string.type)}: ${characterEntity.type}"
        val genderText = "${getString(R.string.gender)}: ${characterEntity.gender}"
        val originText = if (characterEntity.origin.name == getString(R.string.unknown)) {
            "${getString(R.string.origin)}: ${characterEntity.origin.name}"
        } else {
            Html.fromHtml("${getString(R.string.origin)}: <u>${characterEntity.origin.name}</u>")
        }
        val locationText = if (characterEntity.location.name == getString(R.string.unknown)) {
            "${getString(R.string.location)}: ${characterEntity.location.name}"
        } else {
            Html.fromHtml("${getString(R.string.location)}: <u>${characterEntity.location.name}</u>")
        }
        binding.characterDetailsStatusTextView.text = statusText
        binding.characterDetailsSpeciesTextView.text = speciesText
        binding.characterDetailsTypeTextView.text = typeText
        binding.characterDetailsGenderTextView.text = genderText
        binding.characterDetailsOriginTextView.text = originText
        binding.characterDetailsLocationTextView.text = locationText
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.characterDetailsProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.characterDetailsRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}