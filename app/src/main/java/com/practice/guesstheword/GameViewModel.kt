package com.practice.guesstheword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // list of all possible words to guess
    private val words = listOf(
        "friendship",
        "organization",
        "explanation",
        "knowledge",
        "newspaper",
        "recommendation",
        "technology",
        "baseball",
        "midnight",
        "language",
        "community",
        "temperature",
        "presentation",
        "professor",
        "permission",
        "appearance",
        "computer",
        "assistance",
        "association",
        "possibility"
    )

    // the word the user has to guess
    private val secretWord = words.random().uppercase()

    // the word displayed to user during the game
    private val _secretWordDisplayed = MutableLiveData<String>()
    val secretWordDisplayed: LiveData<String>
        get() = _secretWordDisplayed

    // string with all correct guesses
    private var correctGuesses = ""

    // string with all incorrect guesses displayed to user
    private val _incorrectGuesses = MutableLiveData("")
    val incorrectGuesses: LiveData<String>
        get() = _incorrectGuesses

    // amount of lives left
    private val _lives = MutableLiveData(8)
    val lives: LiveData<Int>
        get() = _lives

    // when this value is true, the game has finished with either win or loss of the player
    private val _gameOver = MutableLiveData(false)
    val gameOver: LiveData<Boolean>
        get() = _gameOver

    init {
        _secretWordDisplayed.value = deriveSecretWordDisplayed()
    }

    // get the current display of the secret word
    private fun deriveSecretWordDisplayed(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    // Check if the letter is in the secret word. If it is, return it
    private fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        else -> "_"
    }

    // check the user guess
    fun makeGuess(guess: String) {
        if (secretWord.contains(guess)) {
            // if the user guessed right, update the
            // displayed word and the list of correct guesses
            correctGuesses += guess
            _secretWordDisplayed.value = deriveSecretWordDisplayed()
        } else {
            // if the user guessed wrong, update the list of
            // incorrect guesses and decrease the number of lives
            _incorrectGuesses.value += "$guess "
            _lives.value = lives.value?.minus(1)
        }
        if (userLost() || userWon()) _gameOver.value = true
    }

    // check if the user has won
    private fun userWon() = secretWord.equals(secretWordDisplayed.value, true)

    // check if the user has lost
    private fun userLost() = (lives.value ?: 0) <= 0

    // get the message to send to the result fragment
    fun messageToSend(): String {
        var message = ""
        if (userWon()) message = "Вы победили! "
        else if (userLost()) message = "Вы проиграли! "
        message += "Загаданное слово было $secretWord."
        return message
    }

    // check if the guess is a repeat of a previous guess
    fun isGuessRepeated(guess: String) = ((incorrectGuesses.value?.contains(guess) ?: false)
            || correctGuesses.contains(guess))

    // check if the guess is valid
    fun isGuessValid(guess: String) = guess.length == 1 && guess in "A".."Z"

    // finish the game without guessing
    fun finishGame() {
        _gameOver.value = true
    }
}