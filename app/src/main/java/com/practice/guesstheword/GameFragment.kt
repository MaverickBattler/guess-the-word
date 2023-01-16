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

        updateScreen()

        binding.guessButton.setOnClickListener {
            val guess = binding.letterEdittext.text.toString().uppercase()
            if (viewModel.isGuessRepeated(guess) && viewModel.isGuessValid(guess))
                showRepeatGuessNotice()
            else if (viewModel.isGuessValid(guess)) {
                viewModel.makeGuess(guess)
                updateScreen()
                // if user has won or lost
                if (viewModel.userWon() || viewModel.userLost()) {
                    // navigate to ResultFragment
                    val action =
                        GameFragmentDirections.actionGameFragmentToResultFragment(
                            viewModel.messageToSend()
                        )
                    view.findNavController().navigate(action)
                }
            }
            binding.letterEdittext.text = null
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // update the text on screen
    private fun updateScreen() {
        binding.wordTextview.text = viewModel.secretWordDisplayed
        binding.livesTextview.text = getString(R.string.you_have_lives_left)
        binding.livesAmountTextview.text = viewModel.lives.toString()
        binding.incorrectGuessesTextview.text =
            getString(R.string.incorrect_guesses)
        binding.incorrectGuessesListTextview.text = viewModel.incorrectGuesses
        setLivesAmountColor()
    }

    // Set the lives amount textview color based on the amount left
    private fun setLivesAmountColor() {
        when (viewModel.lives) {
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

    // Show a toast with the info that the guess is a repeat of a previous guess
    private fun showRepeatGuessNotice() {
        Toast.makeText(activity, R.string.you_have_already_tried_this_letter, Toast.LENGTH_SHORT)
            .show()
    }
}