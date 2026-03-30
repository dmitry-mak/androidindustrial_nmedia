package ru.netology.nmedia

import org.junit.Assert.*
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class DiffMethodsTest {
//    @Test
//    fun shouldGetCurrentDateFormatted() {
//        val result = DiffMethods.getCurrentDateFormatted()
//        val pattern = """\d{1,2} [A-Za-z]+, \d{2}:\d{2}""".toRegex()
//        assertTrue(
//            "Expected format like '7 February, 11:30', but got: $result",
//            pattern.matches(result)
//        )
//    }

    @Test
    fun shouldGetCurrentDateFormatted() {
        val previousTimeZone = TimeZone.getDefault()

        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            val millis = 0L

            val result = DiffMethods.getCurrentDateFormatted(
                millisPublished = millis,
                locale = Locale.ENGLISH,
                pattern = "dd MMMM, HH:mm"
            )
            assertEquals("01 January, 00:00", result)
        }finally {
            TimeZone.setDefault(previousTimeZone)
        }
    }
}