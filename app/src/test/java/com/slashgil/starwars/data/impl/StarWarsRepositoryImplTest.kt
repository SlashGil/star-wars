package com.slashgil.starwars.data.impl

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.StarWarsEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StarWarsRepositoryImplTest {

    private val api: SwapiService = mockk()
    private lateinit var repository: StarWarsRepositoryImpl

    @Before
    fun setup() {
        repository = StarWarsRepositoryImpl(api)
    }

    @Test
    fun `getEntities PEOPLE returns mapped PersonEntity list`() = runTest {
        val personDto = PersonDto(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hairColor = "blond",
            skinColor = "fair",
            eyeColor = "blue",
            birthYear = "19BBY",
            gender = "male",
            homeworld = "https://swapi.info/api/planets/1/",
            url = "https://swapi.info/api/people/1/"
        )
        coEvery { api.getPeople() } returns listOf(personDto)

        val result = repository.getEntities(Category.PEOPLE, 1)

        assertTrue(result.isSuccess)
        val entities = result.getOrNull()
        assertEquals(1, entities?.size)
        assertTrue(entities?.first() is StarWarsEntity.PersonEntity)
        assertEquals("Luke Skywalker", entities?.first()?.name)
        coVerify(exactly = 1) { api.getPeople() }
    }

    @Test
    fun `getEntities STARSHIPS returns mapped StarshipEntity list`() = runTest {
        val starshipDto = StarshipDto(
            name = "X-wing",
            model = "T-65B x-wing",
            manufacturer = "Incom Corporation",
            costInCredits = "149999",
            length = "12.5",
            maxAtmospheringSpeed = "1050",
            crew = "1",
            passengers = "0",
            cargoCapacity = "110",
            consumables = "1 week",
            hyperdriveRating = "1.0",
            mglt = "100",
            starshipClass = "Starfighter",
            url = "https://swapi.info/api/starships/12/"
        )
        coEvery { api.getStarships() } returns listOf(starshipDto)

        val result = repository.getEntities(Category.STARSHIPS, 1)

        assertTrue(result.isSuccess)
        val entities = result.getOrNull()
        assertEquals(1, entities?.size)
        assertTrue(entities?.first() is StarWarsEntity.StarshipEntity)
        assertEquals("X-wing", entities?.first()?.name)
        coVerify(exactly = 1) { api.getStarships() }
    }

    @Test
    fun `getEntities PLANETS returns mapped PlanetEntity list`() = runTest {
        val planetDto = PlanetDto(
            name = "Tatooine",
            rotationPeriod = "23",
            orbitalPeriod = "304",
            diameter = "10465",
            climate = "arid",
            gravity = "1 standard",
            terrain = "desert",
            surfaceWater = "1",
            population = "200000",
            url = "https://swapi.info/api/planets/1/"
        )
        coEvery { api.getPlanets() } returns listOf(planetDto)

        val result = repository.getEntities(Category.PLANETS, 1)

        assertTrue(result.isSuccess)
        val entities = result.getOrNull()
        assertEquals(1, entities?.size)
        assertTrue(entities?.first() is StarWarsEntity.PlanetEntity)
        assertEquals("Tatooine", entities?.first()?.name)
        coVerify(exactly = 1) { api.getPlanets() }
    }

    @Test
    fun `getEntities SPECIES returns mapped SpeciesEntity list`() = runTest {
        val speciesDto = SpeciesDto(
            name = "Wookie",
            classification = "mammal",
            designation = "sentient",
            averageHeight = "210",
            skinColors = "gray, black",
            hairColors = "brown, black",
            eyeColors = "blue, green",
            averageLifespan = "400",
            homeworld = "https://swapi.info/api/planets/14/",
            language = "Shyriiwook",
            url = "https://swapi.info/api/species/3/"
        )
        coEvery { api.getSpecies() } returns listOf(speciesDto)

        val result = repository.getEntities(Category.SPECIES, 1)

        assertTrue(result.isSuccess)
        val entities = result.getOrNull()
        assertEquals(1, entities?.size)
        assertTrue(entities?.first() is StarWarsEntity.SpeciesEntity)
        assertEquals("Wookie", entities?.first()?.name)
        coVerify(exactly = 1) { api.getSpecies() }
    }

    @Test
    fun `getEntities FILMS returns mapped FilmEntity list`() = runTest {
        val filmDto = FilmDto(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/"
        )
        coEvery { api.getFilms() } returns listOf(filmDto)

        val result = repository.getEntities(Category.FILMS, 1)

        assertTrue(result.isSuccess)
        val entities = result.getOrNull()
        assertEquals(1, entities?.size)
        assertTrue(entities?.first() is StarWarsEntity.FilmEntity)
        assertEquals("A New Hope", entities?.first()?.name)
        coVerify(exactly = 1) { api.getFilms() }
    }

    @Test
    fun `getEntityById PEOPLE returns mapped PersonEntity`() = runTest {
        val personDto = PersonDto(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hairColor = "blond",
            skinColor = "fair",
            eyeColor = "blue",
            birthYear = "19BBY",
            gender = "male",
            homeworld = "https://swapi.info/api/planets/1/",
            url = "https://swapi.info/api/people/1/"
        )
        coEvery { api.getPerson("1") } returns personDto

        val result = repository.getEntityById(Category.PEOPLE, "1")

        assertTrue(result.isSuccess)
        val entity = result.getOrNull()
        assertTrue(entity is StarWarsEntity.PersonEntity)
        assertEquals("Luke Skywalker", entity?.name)
        coVerify(exactly = 1) { api.getPerson("1") }
    }

    @Test
    fun `getEntityById STARSHIPS returns mapped StarshipEntity`() = runTest {
        val starshipDto = StarshipDto(
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
        coEvery { api.getStarship("10") } returns starshipDto

        val result = repository.getEntityById(Category.STARSHIPS, "10")

        assertTrue(result.isSuccess)
        val entity = result.getOrNull()
        assertTrue(entity is StarWarsEntity.StarshipEntity)
        assertEquals("Millennium Falcon", entity?.name)
        coVerify(exactly = 1) { api.getStarship("10") }
    }

    @Test
    fun `searchEntities STARSHIPS returns filtered results`() = runTest {
        val s1 = StarshipDto(name = "Death Star", url = "https://swapi.info/api/starships/9/")
        val s2 = StarshipDto(name = "Millennium Falcon", url = "https://swapi.info/api/starships/10/")
        coEvery { api.getStarships() } returns listOf(s1, s2)

        val result = repository.searchEntities(Category.STARSHIPS, "Falcon")

        assertTrue(result.isSuccess)
        val entities = result.getOrNull()
        assertEquals(1, entities?.size)
        assertEquals("Millennium Falcon", entities?.first()?.name)
    }

    @Test
    fun `getEntities returns failure when api throws exception`() = runTest {
        val exception = RuntimeException("Network Error")
        coEvery { api.getPeople() } throws exception

        val result = repository.getEntities(Category.PEOPLE, 1)

        assertTrue(result.isFailure)
        assertEquals("Network Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getPeople backward compatibility delegates to getEntities`() = runTest {
        val dto = PersonDto(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hairColor = "blond",
            skinColor = "fair",
            eyeColor = "blue",
            birthYear = "19BBY",
            gender = "male",
            homeworld = "https://swapi.info/api/planets/1/",
            url = "https://swapi.info/api/people/1/"
        )
        coEvery { api.getPeople() } returns listOf(dto)

        val result = repository.getPeople(1)

        assertTrue(result.isSuccess)
        val people = result.getOrNull()
        assertEquals(1, people?.size)
        assertEquals("Luke Skywalker", people?.first()?.name)
    }
}
