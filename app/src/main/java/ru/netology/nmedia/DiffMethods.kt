package ru.netology.nmedia

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DiffMethods {
    /**
     * Метод получает на вход число в формате 1 или 100 или 1000 или 10000
     * Метод должен вернуть число в формате 1.0K, 10K, 100K, 1M
     *
     */
    fun convertNumber(number: Int): String {
        return when {
            number < 1_000 -> number.toString()
            number < 10_000 -> {
                val thousands = number / 1000
                val hundreds = (number % 1000) / 100
                "${thousands}.${hundreds}K"
            }

            number < 1_000_000 -> {
                "${number / 1_000}K"
            }

            else -> {
                val millions = number / 1_000_000
                val hundredThousands = (number % 1_000_000) / 100_000
                "${millions}.${hundredThousands}M"
            }
        }
    }

    fun getCurrentDateFormatted(
        millisPublished: Long,
        locale: Locale = Locale.ENGLISH,
        pattern: String = "d MMMM, HH:mm"
    ): String {
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(Date(millisPublished))
    }
}