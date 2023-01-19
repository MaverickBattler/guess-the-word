package com.practice.guesstheword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.practice.guesstheword.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    // view binding properties
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        // Set the data binding variable
        binding.gameViewModel = viewModel
        // Let the layout respond to the live data updates
        binding.lifecycleOwner = viewLifecycleOwner
        // Set LiveData observers
        viewModel.lives.observe(viewLifecycleOwner) { newValue ->
            // Set the lives amount textview color based on the amount left
            when (newValue) {
                in 6..8 -> binding.livesAmountTextview.setTextColor(
                    ContextCompat.getColor(
                        binding.livesAmountTextview.context,
                        R.color.green
                    )
                )
                in 3..5 -> binding.livesAmountTextview.setTextColor(
                    ContextCompat.getColor(
                        binding.livesAmountTextview.context,
                        R.color.yellow
                    )
                )
                else -> binding.livesAmountTextview.setTextColor(
                    ContextCompat.getColor(
                        binding.livesAmountTextview.context,
                        R.color.red
                    )
                )
            }
        }
        viewModel.gameOver.observe(viewLifecycleOwner) { newValue ->
            if (newValue) {
                // navigate to ResultFragment
                val action =
                    GameFragmentDirections.actionGameFragmentToResultFragment(
                        viewModel.messageToSend()
                    )
                view.findNavController().navigate(action)
            }
        }

        binding.guessButton.setOnClickListener {
            val guess = binding.letterEdittext.text.toString().uppercase()
            if (viewModel.isGuessValid(guess) && !viewModel.isGuessRepeated(guess))
                viewModel.makeGuess(guess)
            else if (viewModel.isGuessValid(guess))
                showRepeatGuessNotice()
            binding.letterEdittext.text = null
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Show a toast with the info that the guess is a repeat of a previous guess
    private fun showRepeatGuessNotice() {
        Toast.makeText(activity, R.string.you_have_already_tried_this_letter, Toast.LENGTH_SHORT)
            .show()
    }
}