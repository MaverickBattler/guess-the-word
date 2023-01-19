package com.practice.guesstheword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.practice.guesstheword.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    // view binding properties
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ResultViewModel
    private lateinit var viewModelFactory: ResultViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val view = binding.root

        val result = ResultFragmentArgs.fromBundle(requireArguments()).result
        viewModelFactory = ResultViewModelFactory(result)
        viewModel = ViewModelProvider(this, viewModelFactory)[ResultViewModel::class.java]
        binding.resultViewModel = viewModel

        binding.startNewGameButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_result_fragment_to_game_fragment)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}