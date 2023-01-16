package com.practice.guesstheword

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
    var secretWordDisplayed = ""

    // string with all correct guesses
    private var correctGuesses = ""

    // string with all incorrect guesses displayed to user
    var incorrectGuesses = ""

    // amount of lives left
    var lives = 8

    init {
        secretWordDisplayed = deriveSecretWordDisplayed()
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
            secretWordDisplayed = deriveSecretWordDisplayed()
        } else {
            // if the user guessed wrong, update the list of
            // incorrect guesses and decrease the number of lives
            incorrectGuesses += "$guess "
            lives--
        }
    }

    // check if the user has won
    fun userWon() = secretWord.equals(secretWordDisplayed, true)

    // check if the user has lost
    fun userLost() = lives <= 0

    // get the message to send to the result fragment
    fun messageToSend(): String {
        var message = ""
        if (userWon()) message = "Вы победили!"
        else if (userLost()) message = "Вы проиграли!"
        message += " Загаданное слово было $secretWord."
        return message
    }

    // check if the guess is a repeat of a previous guess
    fun isGuessRepeated(guess: String) = (incorrectGuesses.contains(guess)
            || correctGuesses.contains(guess))

    // check if the guess is valid
    fun isGuessValid(guess: String) = guess.length == 1 && guess in "A".."Z"
}