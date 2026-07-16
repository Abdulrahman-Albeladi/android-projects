package com.example.project3

/**
 * Applies a Caesar-style shift to uppercase characters.
 *
 * Characters shifted beyond `Z` wrap backward by one alphabet length.
 */
class Encryption(private val number: Int) {
    fun encryptText(input: String): String = input.map { char ->
        val shifted = char + number
        if (shifted <= 'Z') shifted else shifted - ALPHABET_SIZE
    }.joinToString("")

    private companion object {
        const val ALPHABET_SIZE = 26
    }
}
