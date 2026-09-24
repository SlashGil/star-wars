package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.StarWarsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetEntitiesUseCaseImplTest {

    private val repository: StarWarsRepository = mockk()
    private lateinit var useCase: GetEntitiesUseCaseImpl

    @Before
    fun setup() {
        useCase = GetEntitiesUseCaseImpl(repository)
    }

    @Test
    fun `invoke calls repository getEntities with category and page`() = runTest {
        val starship = StarWarsEntity.StarshipEntity(
            name = "Millennium Falcon",
            model = "YT-1300 light freighter",
            manufacturer = "Corellian Engineering Corporation",
            costInCredits = "100000",
            length = "34.37",
            maxAtmospheringSpeed = "1050",
            crew = "4",
            passengers = "6",
            cargoCapacity = "100000",
            consumables = "2 months",
            hyperdriveRating = "0.5",
            mglt = "75",
            starshipClass = "Light freighter",
            url = "https://swapi.info/api/starships/10/"
        )
        coEvery { repository.getEntities(Category.STARSHIPS, 1) } returns Result.success(listOf(starship))

        val result = useCase(Category.STARSHIPS, 1)

        assertTrue(result.isSuccess)
        assertEquals(listOf(starship), result.getOrNull())
        coVerify(exactly = 1) { repository.getEntities(Category.STARSHIPS, 1) }
    }
}
