package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.data.contract.PersonRepository
import com.slashgil.starwars.domain.contract.Person
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchPeopleUseCaseImplTest {

    private val repository: PersonRepository = mockk()
    private lateinit var searchPeopleUseCase: SearchPeopleUseCaseImpl

    @Before
    fun setup() {
        searchPeopleUseCase = SearchPeopleUseCaseImpl(repository)
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
