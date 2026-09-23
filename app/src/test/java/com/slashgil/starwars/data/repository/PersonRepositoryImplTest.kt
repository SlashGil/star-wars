package com.slashgil.starwars.data.repository

import com.slashgil.starwars.data.remote.api.SwapiService
import com.slashgil.starwars.data.remote.model.PersonDto
import com.slashgil.starwars.data.remote.model.PersonResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PersonRepositoryImplTest {

    private val api: SwapiService = mockk()
    private lateinit var repository: PersonRepositoryImpl

    @Before
    fun setup() {
        repository = PersonRepositoryImpl(api)
    }

    @Test
    fun `getPeople returns mapped domain models when api call is successful`() = runTest {
        val dto = PersonDto(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hairColor = "blond",
            skinColor = "fair",
            eyeColor = "blue",
            birthYear = "19BBY",
            gender = "male",
            homeworld = "https://swapi.dev/api/planets/1/",
            url = "https://swapi.dev/api/people/1/"
        )
        val response = PersonResponseDto(count = 1, results = listOf(dto))
        coEvery { api.getPeople(1) } returns response

        val result = repository.getPeople(1)

        assertTrue(result.isSuccess)
        val people = result.getOrNull()
        assertEquals(1, people?.size)
        assertEquals("Luke Skywalker", people?.first()?.name)
        coVerify(exactly = 1) { api.getPeople(1) }
    }

    @Test
    fun `getPeople returns failure when api throws exception`() = runTest {
        val exception = RuntimeException("Network Error")
        coEvery { api.getPeople(1) } throws exception

        val result = repository.getPeople(1)

        assertTrue(result.isFailure)
        assertEquals("Network Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `searchPeople returns mapped domain models when api call is successful`() = runTest {
        val dto = PersonDto(
            name = "Darth Vader",
            height = "202",
            mass = "136",
            hairColor = "none",
            skinColor = "white",
            eyeColor = "yellow",
            birthYear = "41.9BBY",
            gender = "male",
            homeworld = "https://swapi.dev/api/planets/1/",
            url = "https://swapi.dev/api/people/4/"
        )
        val response = PersonResponseDto(count = 1, results = listOf(dto))
        coEvery { api.searchPeople("Darth") } returns response

        val result = repository.searchPeople("Darth")

        assertTrue(result.isSuccess)
        val people = result.getOrNull()
        assertEquals(1, people?.size)
        assertEquals("Darth Vader", people?.first()?.name)
        coVerify(exactly = 1) { api.searchPeople("Darth") }
    }

    @Test
    fun `searchPeople returns failure when api throws exception`() = runTest {
        val exception = RuntimeException("API Error")
        coEvery { api.searchPeople("Darth") } throws exception

        val result = repository.searchPeople("Darth")

        assertTrue(result.isFailure)
        assertEquals("API Error", result.exceptionOrNull()?.message)
    }
}
