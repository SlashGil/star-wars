package com.slashgil.starwars.domain.usecase

import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.domain.repository.PersonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPeopleUseCaseTest {

    private val repository: PersonRepository = mockk()
    private lateinit var getPeopleUseCase: GetPeopleUseCase

    @Before
    fun setup() {
        getPeopleUseCase = GetPeopleUseCase(repository)
    }

    @Test
    fun `invoke with default parameter calls repository getPeople with page 1`() = runTest {
        val people = listOf(
            Person("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male", "Tatooine", "1")
        )
        coEvery { repository.getPeople(1) } returns Result.success(people)

        val result = getPeopleUseCase()

        assertTrue(result.isSuccess)
        assertEquals(people, result.getOrNull())
        coVerify(exactly = 1) { repository.getPeople(1) }
    }

    @Test
    fun `invoke with specified page calls repository getPeople with that page`() = runTest {
        val page = 2
        val people = listOf(
            Person("Anakin Skywalker", "188", "84", "blond", "fair", "blue", "41.9BBY", "male", "Tatooine", "11")
        )
        coEvery { repository.getPeople(page) } returns Result.success(people)

        val result = getPeopleUseCase(page)

        assertTrue(result.isSuccess)
        assertEquals(people, result.getOrNull())
        coVerify(exactly = 1) { repository.getPeople(page) }
    }
}
