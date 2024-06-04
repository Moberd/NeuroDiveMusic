package com.sfedu_mmcs.neurodivemusic.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentSettingsBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    val args: TrackInfoFragmentArgs by navArgs()
    private val settingsModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkboxes: List<CheckBox> = listOf(
            binding.rock,
            binding.pop,
            binding.jazz,
            binding.latino,
            binding.ambient
        )

        checkboxes.forEach { checkbox ->
            val genre = resources.getResourceEntryName(checkbox.id)
            checkbox.isChecked = settingsModel.genresList.contains(genre)
        }

        Log.d("SettingsFragment", "genresList: [${settingsModel.genresList.joinToString(", ")}]")

        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                val genre = resources.getResourceEntryName(checkbox.id)
                if (isChecked) {
                    settingsModel.genresList.add(genre)
                } else {
                    settingsModel.genresList.remove(genre)
                }
                Log.d("SettingsFragment", "genresList: [${settingsModel.genresList.joinToString(", ")}]")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}