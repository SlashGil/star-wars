package com.slashgil.starwars.domain.impl

import android.content.Context
import android.content.res.AssetManager
import com.slashgil.starwars.domain.contract.Category
import io.mockk.every
import io.mockk.mockk
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterImageResolverImplTest {

    private lateinit var resolverNoContext: CharacterImageResolverImpl

    @Before
    fun setup() {
        resolverNoContext = CharacterImageResolverImpl(context = null)
    }

    @Test
    fun resolveCharacterImageUrl_withoutContext_fallsBackToRemoteUrl() {
        assertEquals(
            "https://starwars-visualguide.com/assets/img/characters/1.jpg",
            resolverNoContext.resolveCharacterImageUrl("https://swapi.info/api/people/1/")
        )
        assertEquals("", resolverNoContext.resolveCharacterImageUrl(""))
    }

    @Test
    fun resolveImageUrl_withoutContext_fallsBackToRemoteUrlForCategories() {
        assertEquals(
            "https://starwars-visualguide.com/assets/img/starships/10.jpg",
            resolverNoContext.resolveImageUrl(Category.STARSHIPS, "https://swapi.info/api/starships/10/")
        )
        assertEquals(
            "https://starwars-visualguide.com/assets/img/planets/2.jpg",
            resolverNoContext.resolveImageUrl(Category.PLANETS, "https://swapi.info/api/planets/2/")
        )
        assertEquals(
            "https://starwars-visualguide.com/assets/img/species/3.jpg",
            resolverNoContext.resolveImageUrl(Category.SPECIES, "https://swapi.info/api/species/3/")
        )
        assertEquals(
            "https://starwars-visualguide.com/assets/img/films/1.jpg",
            resolverNoContext.resolveImageUrl(Category.FILMS, "https://swapi.info/api/films/1/")
        )
    }

    @Test
    fun resolveImageUrl_withContext_andExistingAsset_returnsFileAndroidAssetUrl() {
        val mockContext: Context = mockk()
        val mockAssets: AssetManager = mockk()

        every { mockContext.assets } returns mockAssets
        every { mockAssets.open("people/1.jpg") } returns ByteArrayInputStream(byteArrayOf(1, 2, 3))

        val resolver = CharacterImageResolverImpl(context = mockContext)
        val url = resolver.resolveImageUrl(Category.PEOPLE, "https://swapi.info/api/people/1/")

        assertEquals("file:///android_asset/people/1.jpg", url)
    }

    @Test
    fun resolveImageUrl_withContext_andAliasFolder_returnsFileAndroidAssetUrl() {
        val mockContext: Context = mockk()
        val mockAssets: AssetManager = mockk()

        every { mockContext.assets } returns mockAssets
        every { mockAssets.open(any()) } throws FileNotFoundException("Not found")
        every { mockAssets.open("characters/1.jpg") } returns ByteArrayInputStream(byteArrayOf(1))
        every { mockAssets.open("vehicles/4.jpg") } returns ByteArrayInputStream(byteArrayOf(2))
        every { mockAssets.open("hero/starships.jpg") } returns ByteArrayInputStream(byteArrayOf(3))

        val resolver = CharacterImageResolverImpl(context = mockContext)

        assertEquals(
            "file:///android_asset/characters/1.jpg",
            resolver.resolveImageUrl(Category.PEOPLE, "https://swapi.info/api/people/1/")
        )
        assertEquals(
            "file:///android_asset/vehicles/4.jpg",
            resolver.resolveImageUrl(Category.STARSHIPS, "https://swapi.info/api/starships/4/")
        )
        assertEquals(
            "file:///android_asset/hero/starships.jpg",
            resolver.resolveImageUrl(Category.STARSHIPS, "https://swapi.info/api/starships/starships/")
        )
    }

    @Test
    fun resolveImageUrl_withContext_andDifferentExtensions_returnsFileAndroidAssetUrl() {
        val mockContext: Context = mockk()
        val mockAssets: AssetManager = mockk()

        every { mockContext.assets } returns mockAssets
        every { mockAssets.open(any()) } throws FileNotFoundException("Not found")
        every { mockAssets.open("planets/2.png") } returns ByteArrayInputStream(byteArrayOf(1))
        every { mockAssets.open("species/3.webp") } returns ByteArrayInputStream(byteArrayOf(2))
        every { mockAssets.open("films/1.jpeg") } returns ByteArrayInputStream(byteArrayOf(3))

        val resolver = CharacterImageResolverImpl(context = mockContext)

        assertEquals(
            "file:///android_asset/planets/2.png",
            resolver.resolveImageUrl(Category.PLANETS, "https://swapi.info/api/planets/2/")
        )
        assertEquals(
            "file:///android_asset/species/3.webp",
            resolver.resolveImageUrl(Category.SPECIES, "https://swapi.info/api/species/3/")
        )
        assertEquals(
            "file:///android_asset/films/1.jpeg",
            resolver.resolveImageUrl(Category.FILMS, "https://swapi.info/api/films/1/")
        )
    }

    @Test
    fun resolveImageUrl_withContext_andMissingAsset_fallsBackToRemoteUrl() {
        val mockContext: Context = mockk()
        val mockAssets: AssetManager = mockk()

        every { mockContext.assets } returns mockAssets
        every { mockAssets.open(any()) } throws FileNotFoundException("Asset not found")

        val resolver = CharacterImageResolverImpl(context = mockContext)
        val url = resolver.resolveImageUrl(Category.STARSHIPS, "https://swapi.info/api/starships/999/")

        assertEquals("https://starwars-visualguide.com/assets/img/starships/999.jpg", url)
    }

    @Test
    fun resolveFilmImageUrl_generatesCorrectUrl() {
        assertEquals(
            "https://starwars-visualguide.com/assets/img/films/1.jpg",
            resolverNoContext.resolveFilmImageUrl("https://swapi.info/api/films/1/")
        )
        assertEquals("", resolverNoContext.resolveFilmImageUrl(""))
    }

    @Test
    fun resolveFilmTitle_returnsCorrectTitle() {
        assertEquals("Episode IV: A New Hope", resolverNoContext.resolveFilmTitle("https://swapi.info/api/films/1/"))
        assertEquals("Episode V: The Empire Strikes Back", resolverNoContext.resolveFilmTitle("https://swapi.info/api/films/2/"))
        assertEquals("Episode 9", resolverNoContext.resolveFilmTitle("https://swapi.info/api/films/9/"))
    }

    @Test
    fun resolveFilm_returnsFilmDomainModel() {
        val film = resolverNoContext.resolveFilm("https://swapi.info/api/films/1/")
        assertEquals("https://swapi.info/api/films/1/", film.url)
        assertEquals("Episode IV: A New Hope", film.title)
        assertEquals("https://starwars-visualguide.com/assets/img/films/1.jpg", film.posterUrl)
        assertEquals("1", film.id)
    }

    @Test
    fun resolveCharacterName_returnsCorrectName() {
        assertEquals("Luke Skywalker", resolverNoContext.resolveCharacterName("https://swapi.info/api/people/1/"))
        assertEquals("C-3PO", resolverNoContext.resolveCharacterName("https://swapi.info/api/people/2/"))
        assertEquals("Darth Vader", resolverNoContext.resolveCharacterName("https://swapi.info/api/people/4/"))
        assertEquals("Character #999", resolverNoContext.resolveCharacterName("https://swapi.info/api/people/999/"))
        assertEquals("Han Solo", resolverNoContext.resolveCharacterName("Han Solo"))
    }

    @Test
    fun resolvePlanetName_returnsCorrectName() {
        assertEquals("Tatooine", resolverNoContext.resolvePlanetName("https://swapi.info/api/planets/1/"))
        assertEquals("Alderaan", resolverNoContext.resolvePlanetName("https://swapi.info/api/planets/2/"))
        assertEquals("Planet #999", resolverNoContext.resolvePlanetName("https://swapi.info/api/planets/999/"))
        assertEquals("Endor", resolverNoContext.resolvePlanetName("Endor"))
    }

    @Test
    fun resolveSpeciesName_returnsCorrectName() {
        assertEquals("Human", resolverNoContext.resolveSpeciesName("https://swapi.info/api/species/1/"))
        assertEquals("Droid", resolverNoContext.resolveSpeciesName("https://swapi.info/api/species/2/"))
        assertEquals("Species #999", resolverNoContext.resolveSpeciesName("https://swapi.info/api/species/999/"))
        assertEquals("Wookiee", resolverNoContext.resolveSpeciesName("Wookiee"))
    }

    @Test
    fun resolveStarshipName_returnsCorrectName() {
        assertEquals("Millennium Falcon", resolverNoContext.resolveStarshipName("https://swapi.info/api/starships/10/"))
        assertEquals("X-wing", resolverNoContext.resolveStarshipName("https://swapi.info/api/starships/12/"))
        assertEquals("Starship #999", resolverNoContext.resolveStarshipName("https://swapi.info/api/starships/999/"))
        assertEquals("TIE Fighter", resolverNoContext.resolveStarshipName("TIE Fighter"))
    }
}
