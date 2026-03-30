package ru.netology.nmedia

import org.junit.Assert.*
import org.junit.Test
import ru.netology.nmedia.DiffMethods.convertNumber

class ConvertNumberTest {
    @Test
    fun shouldConvertNumbers(){
        assertEquals("1.0K", convertNumber(1000))
        assertEquals("1", convertNumber(1))
        assertEquals("1.0K", convertNumber(1_000))
        assertEquals("1.1K", convertNumber(1125))
        assertEquals("6.3K", convertNumber(6399))
        assertEquals("10K", convertNumber(10_000))
        assertEquals("999K", convertNumber(999_999))
        assertEquals("1.3M", convertNumber(1_300_000))
        assertEquals("10.1M", convertNumber(10_100_000))
    }

}