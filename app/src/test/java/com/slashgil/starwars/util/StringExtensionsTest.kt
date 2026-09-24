package com.slashgil.starwars.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun capitalizeWords_capitalizesSingleWord() {
        assertEquals("Blue", "blue".capitalizeWords())
        assertEquals("Male", "male".capitalizeWords())
        assertEquals("Blond", "blond".capitalizeWords())
        assertEquals("Unknown", "unknown".capitalizeWords())
    }

    @Test
    fun capitalizeWords_capitalizesMultipleWordsAndDelimiters() {
        assertEquals("Arid, Temperate", "arid, temperate".capitalizeWords())
        assertEquals("Fair, Green-Yellow", "fair, green-yellow".capitalizeWords())
    }

    @Test
    fun capitalizeWords_handlesNA() {
        assertEquals("N/A", "n/a".capitalizeWords())
        assertEquals("N/A", "N/A".capitalizeWords())
        assertEquals("N/A, Unknown", "n/a, unknown".capitalizeWords())
    }

    @Test
    fun capitalizeWords_handlesYearDesignations() {
        assertEquals("19BBY", "19bby".capitalizeWords())
        assertEquals("19BBY", "19BBY".capitalizeWords())
    }

    @Test
    fun capitalizeWords_handlesBlankAndEmpty() {
        assertEquals("", "".capitalizeWords())
        assertEquals("   ", "   ".capitalizeWords())
    }
}
