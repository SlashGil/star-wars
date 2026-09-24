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

class SearchEntitiesUseCaseImplTest {

    private val repository: StarWarsRepository = mockk()
    private lateinit var useCase: SearchEntitiesUseCaseImpl

    @Before
    fun setup() {
        useCase = SearchEntitiesUseCaseImpl(repository)
    }

    @Test
    fun `invoke calls repository searchEntities with category and query`() = runTest {
        val planet = StarWarsEntity.PlanetEntity(
            name = "Alderaan",
            rotationPeriod = "24",
            orbitalPeriod = "364",
            diameter = "12500",
            climate = "temperate",
            gravity = "1 standard",
            terrain = "grasslands, mountains",
            surfaceWater = "40",
            population = "2000000000",
            url = "https://swapi.info/api/planets/2/"
        )
        coEvery { repository.searchEntities(Category.PLANETS, "Ald") } returns Result.success(listOf(planet))

        val result = useCase(Category.PLANETS, "Ald")

        assertTrue(result.isSuccess)
        assertEquals(listOf(planet), result.getOrNull())
        coVerify(exactly = 1) { repository.searchEntities(Category.PLANETS, "Ald") }
    }
}
