package com.slashgil.starwars.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SwapiUrlUtilsTest {

    @Test
    fun extractPersonId_extractsIdCorrectly() {
        assertEquals("1", SwapiUrlUtils.extractPersonId("https://swapi.dev/api/people/1/"))
        assertEquals("10", SwapiUrlUtils.extractPersonId("https://swapi.dev/api/people/10"))
        assertEquals("1", SwapiUrlUtils.extractPersonId("1"))
        assertNull(SwapiUrlUtils.extractPersonId(""))
    }

    @Test
    fun getPersonImageUrl_generatesCorrectUrl() {
        assertEquals(
            "https://starwars-visualguide.com/assets/img/characters/1.jpg",
            SwapiUrlUtils.getPersonImageUrl("https://swapi.dev/api/people/1/")
        )
        assertEquals("", SwapiUrlUtils.getPersonImageUrl(""))
    }

    @Test
    fun extractFilmId_extractsIdCorrectly() {
        assertEquals("1", SwapiUrlUtils.extractFilmId("https://swapi.dev/api/films/1/"))
        assertEquals("3", SwapiUrlUtils.extractFilmId("https://swapi.dev/api/films/3"))
        assertEquals("2", SwapiUrlUtils.extractFilmId("2"))
        assertNull(SwapiUrlUtils.extractFilmId(""))
    }

    @Test
    fun getFilmImageUrl_generatesCorrectUrl() {
        assertEquals(
            "https://starwars-visualguide.com/assets/img/films/1.jpg",
            SwapiUrlUtils.getFilmImageUrl("https://swapi.dev/api/films/1/")
        )
        assertEquals("", SwapiUrlUtils.getFilmImageUrl(""))
    }

    @Test
    fun getFilmTitle_returnsCorrectTitle() {
        assertEquals("Episode IV: A New Hope", SwapiUrlUtils.getFilmTitle("https://swapi.dev/api/films/1/"))
        assertEquals("Episode V: The Empire Strikes Back", SwapiUrlUtils.getFilmTitle("https://swapi.dev/api/films/2/"))
        assertEquals("Episode 9", SwapiUrlUtils.getFilmTitle("https://swapi.dev/api/films/9/"))
    }
}
