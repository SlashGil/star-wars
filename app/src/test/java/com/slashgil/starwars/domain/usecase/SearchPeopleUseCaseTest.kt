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

class SearchPeopleUseCaseTest {

    private val repository: PersonRepository = mockk()
    private lateinit var searchPeopleUseCase: SearchPeopleUseCase

    @Before
    fun setup() {
        searchPeopleUseCase = SearchPeopleUseCase(repository)
    }

    @Test
    fun `invoke with query calls repository searchPeople`() = runTest {
        val query = "R2-D2"
        val people = listOf(
            Person("R2-D2", "96", "32", "n/a", "white, blue", "red", "33BBY", "n/a", "Naboo", "2")
        )
        coEvery { repository.searchPeople(query) } returns Result.success(people)

        val result = searchPeopleUseCase(query)

        assertTrue(result.isSuccess)
        assertEquals(people, result.getOrNull())
        coVerify(exactly = 1) { repository.searchPeople(query) }
    }
}
