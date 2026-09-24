package com.slashgil.starwars.domain.impl

import android.content.Context
import android.content.res.AssetManager
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.CharacterImageResolver
import com.slashgil.starwars.domain.contract.Film
import com.slashgil.starwars.domain.contract.StarWarsEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class CharacterImageResolverImpl @Inject constructor(
    @ApplicationContext private val context: Context? = null
) : CharacterImageResolver {

    override fun resolveCharacterImageUrl(characterUrl: String): String {
        return resolveImageUrl(Category.PEOPLE, characterUrl)
    }

    override fun resolveCharacterName(characterUrl: String): String {
        if (characterUrl.isBlank()) return "Character"
        if (!characterUrl.contains("/") && !characterUrl.contains("http") && characterUrl.toIntOrNull() == null) {
            return characterUrl
        }
        val id = extractId(characterUrl) ?: return characterUrl
        return when (id) {
            "1" -> "Luke Skywalker"
            "2" -> "C-3PO"
            "3" -> "R2-D2"
            "4" -> "Darth Vader"
            "5" -> "Leia Organa"
            "6" -> "Owen Lars"
            "7" -> "Beru Whitesun lars"
            "8" -> "R5-D4"
            "9" -> "Biggs Darklighter"
            "10" -> "Obi-Wan Kenobi"
            "11" -> "Anakin Skywalker"
            "12" -> "Wilhuff Tarkin"
            "13" -> "Chewbacca"
            "14" -> "Han Solo"
            "15" -> "Greedo"
            "16" -> "Jabba Desilijic Tiure"
            "17" -> "Wedge Antilles"
            "18" -> "Jek Tono Porkins"
            "19" -> "Yoda"
            "20" -> "Palpatine"
            "21" -> "Boba Fett"
            "22" -> "Lando Calrissian"
            "23" -> "Lobot"
            "24" -> "Mon Mothma"
            "25" -> "Gial Ackbar"
            "26" -> "Nien Nunb"
            "27" -> "Qui-Gon Jinn"
            "28" -> "Nute Gunray"
            "29" -> "Finis Valorum"
            "30" -> "Padmé Amidala"
            "31" -> "Jar Jar Binks"
            "32" -> "Roos Tarpals"
            "33" -> "Rugor Nass"
            "34" -> "Ric Olié"
            "35" -> "Watto"
            "36" -> "Sebulba"
            "37" -> "Quarsh Panaka"
            "38" -> "Shmi Skywalker"
            "39" -> "Darth Maul"
            "40" -> "Ayla Secura"
            "41" -> "Ratts Tyerell"
            "42" -> "Dud Bolt"
            "43" -> "Gasgano"
            "44" -> "Ben Quadinaros"
            "45" -> "Mace Windu"
            "46" -> "Ki-Adi-Mundi"
            "47" -> "Kit Fisto"
            "48" -> "Eeth Koth"
            "49" -> "Adi Gallia"
            "50" -> "Saesee Tiin"
            "51" -> "Yarael Poof"
            "52" -> "Plo Koon"
            "53" -> "Mas Amedda"
            "54" -> "Gregar Typho"
            "55" -> "Cordé"
            "56" -> "Cliegg Lars"
            "57" -> "Poggle the Lesser"
            "58" -> "Luminara Unduli"
            "59" -> "Barriss Offee"
            "60" -> "Dormé"
            "61" -> "Dooku"
            "62" -> "Bail Prestor Organa"
            "63" -> "Jango Fett"
            "64" -> "Zam Wesell"
            "65" -> "Dexter Jettster"
            "66" -> "Lama Su"
            "67" -> "Taun We"
            "68" -> "Jocasta Nu"
            "69" -> "R4-P17"
            "70" -> "Wat Tambor"
            "71" -> "San Hill"
            "72" -> "Shaak Ti"
            "73" -> "Grievous"
            "74" -> "Tarfful"
            "75" -> "Raymus Antilles"
            "76" -> "Sly Moore"
            "77" -> "Tion Medon"
            "78" -> "Finn"
            "79" -> "Rey"
            "80" -> "Poe Dameron"
            "81" -> "BB-8"
            "82" -> "Captain Phasma"
            "83" -> "Padmé Amidala"
            else -> if (id.toIntOrNull() != null) "Character #$id" else characterUrl
        }
    }

    override fun resolveFilmImageUrl(filmUrl: String): String {
        return resolveImageUrl(Category.FILMS, filmUrl)
    }

    override fun resolveFilm(filmUrl: String): Film {
        return Film(
            url = filmUrl,
            title = resolveFilmTitle(filmUrl),
            posterUrl = resolveFilmImageUrl(filmUrl)
        )
    }

    override fun resolveFilmTitle(filmUrl: String): String {
        if (filmUrl.isBlank()) return "Film"
        if (!filmUrl.contains("/") && !filmUrl.contains("http") && filmUrl.toIntOrNull() == null) {
            return filmUrl
        }
        val id = extractId(filmUrl) ?: return filmUrl
        return when (id) {
            "1" -> "Episode IV: A New Hope"
            "2" -> "Episode V: The Empire Strikes Back"
            "3" -> "Episode VI: Return of the Jedi"
            "4" -> "Episode I: The Phantom Menace"
            "5" -> "Episode II: Attack of the Clones"
            "6" -> "Episode III: Revenge of the Sith"
            "7" -> "Episode VII: The Force Awakens"
            else -> if (id.toIntOrNull() != null) "Episode $id" else filmUrl
        }
    }

    override fun resolvePlanetName(planetUrl: String): String {
        if (planetUrl.isBlank()) return "Planet"
        if (!planetUrl.contains("/") && !planetUrl.contains("http") && planetUrl.toIntOrNull() == null) {
            return planetUrl
        }
        val id = extractId(planetUrl) ?: return planetUrl
        return when (id) {
            "1" -> "Tatooine"
            "2" -> "Alderaan"
            "3" -> "Yavin IV"
            "4" -> "Hoth"
            "5" -> "Dagobah"
            "6" -> "Bespin"
            "7" -> "Endor"
            "8" -> "Naboo"
            "9" -> "Coruscant"
            "10" -> "Kamino"
            "11" -> "Geonosis"
            "12" -> "Utapau"
            "13" -> "Mustafar"
            "14" -> "Kashyyyk"
            "15" -> "Polis Massa"
            "16" -> "Mygeeto"
            "17" -> "Felucia"
            "18" -> "Cato Neimoidia"
            "19" -> "Saleucami"
            "20" -> "Stewjon"
            "21" -> "Eriadu"
            "22" -> "Corellia"
            "23" -> "Rodia"
            "24" -> "Nal Hutta"
            "25" -> "Dantooine"
            "26" -> "Bestine IV"
            "27" -> "Ord Mantell"
            "28" -> "Unknown"
            "29" -> "Trandosha"
            "30" -> "Socorro"
            "31" -> "Mon Cala"
            "32" -> "Chandrila"
            "33" -> "Sullust"
            "34" -> "Toydaria"
            "35" -> "Malastare"
            "36" -> "Dathomir"
            "37" -> "Ryloth"
            "38" -> "Aleen Minor"
            "39" -> "Vulpter"
            "40" -> "Troiken"
            "41" -> "Tund"
            "42" -> "Haruun Kal"
            "43" -> "Cerea"
            "44" -> "Glee Anselm"
            "45" -> "Iridonia"
            "46" -> "Tholoth"
            "47" -> "Iktotch"
            "48" -> "Quermia"
            "49" -> "Dorin"
            "50" -> "Champala"
            "51" -> "Mirial"
            "52" -> "Serenno"
            "53" -> "Concord Dawn"
            "54" -> "Zolan"
            "55" -> "Ojha"
            "56" -> "Skako"
            "57" -> "Muunilinst"
            "58" -> "Shili"
            "59" -> "Kalee"
            "60" -> "Umbara"
            else -> if (id.toIntOrNull() != null) "Planet #$id" else planetUrl
        }
    }

    override fun resolveSpeciesName(speciesUrl: String): String {
        if (speciesUrl.isBlank()) return "Species"
        if (!speciesUrl.contains("/") && !speciesUrl.contains("http") && speciesUrl.toIntOrNull() == null) {
            return speciesUrl
        }
        val id = extractId(speciesUrl) ?: return speciesUrl
        return when (id) {
            "1" -> "Human"
            "2" -> "Droid"
            "3" -> "Wookiee"
            "4" -> "Rodian"
            "5" -> "Hutt"
            "6" -> "Yoda's species"
            "7" -> "Trandoshan"
            "8" -> "Mon Calamari"
            "9" -> "Ewok"
            "10" -> "Sullustan"
            "11" -> "Neimoidian"
            "12" -> "Gungan"
            "13" -> "Toydarian"
            "14" -> "Dug"
            "15" -> "Twi'lek"
            "16" -> "Aleena"
            "17" -> "Vulptereen"
            "18" -> "Xexto"
            "19" -> "Toong"
            "20" -> "Cerean"
            "21" -> "Nautolan"
            "22" -> "Zabrak"
            "23" -> "Tholothian"
            "24" -> "Iktotchi"
            "25" -> "Quermian"
            "26" -> "Kel Dor"
            "27" -> "Chagrian"
            "28" -> "Geonosian"
            "29" -> "Mirialan"
            "30" -> "Clawdite"
            "31" -> "Besalisk"
            "32" -> "Kaminoan"
            "33" -> "Skakoan"
            "34" -> "Muun"
            "35" -> "Togruta"
            "36" -> "Kaleesh"
            "37" -> "Pau'an"
            else -> if (id.toIntOrNull() != null) "Species #$id" else speciesUrl
        }
    }

    override fun resolveStarshipName(starshipUrl: String): String {
        if (starshipUrl.isBlank()) return "Starship"
        if (!starshipUrl.contains("/") && !starshipUrl.contains("http") && starshipUrl.toIntOrNull() == null) {
            return starshipUrl
        }
        val id = extractId(starshipUrl) ?: return starshipUrl
        return when (id) {
            "2" -> "CR90 corvette"
            "3" -> "Star Destroyer"
            "5" -> "Sentinel-class landing craft"
            "9" -> "Death Star"
            "10" -> "Millennium Falcon"
            "11" -> "Y-wing"
            "12" -> "X-wing"
            "13" -> "TIE Advanced x1"
            "15" -> "Executor"
            "17" -> "Rebel transport"
            "21" -> "Slave I"
            "22" -> "Imperial shuttle"
            "23" -> "EF76 Nebulon-B escort frigate"
            "27" -> "Calamari Cruiser"
            "28" -> "A-wing"
            "29" -> "B-wing"
            "31" -> "Republic Cruiser"
            "32" -> "Droid control ship"
            "39" -> "Naboo fighter"
            "40" -> "Naboo Royal Starship"
            "41" -> "Scimitar"
            "43" -> "J-type diplomatic barge"
            "47" -> "AA-9 Coruscant freighter"
            "48" -> "Jedi Starfighter"
            "49" -> "H-type Nubian yacht"
            "52" -> "Republic Assault ship"
            "58" -> "Solar Sailor"
            "59" -> "Trade Federation cruiser"
            "61" -> "Theta-class T-2c shuttle"
            "63" -> "Republic attack cruiser"
            "64" -> "Naboo Star Skiff"
            "65" -> "Jedi Interceptor"
            "66" -> "Arc-170"
            "68" -> "Banking Clan frigate"
            "74" -> "Belbullab-22 starfighter"
            "75" -> "V-wing"
            else -> if (id.toIntOrNull() != null) "Starship #$id" else starshipUrl
        }
    }

    override fun resolveEntityImageUrl(entity: StarWarsEntity): String {
        return resolveImageUrl(entity.category, entity.url)
    }

    override fun resolveImageUrl(category: Category, url: String): String {
        val id = extractId(url) ?: return ""

        val localAssetPath = findLocalAssetPath(category, id)
        if (localAssetPath != null) {
            return "file:///android_asset/$localAssetPath"
        }

        val remoteFolder = when (category) {
            Category.PEOPLE -> "characters"
            Category.STARSHIPS -> "starships"
            Category.PLANETS -> "planets"
            Category.SPECIES -> "species"
            Category.FILMS -> "films"
        }
        return "https://starwars-visualguide.com/assets/img/$remoteFolder/$id.jpg"
    }

    private fun findLocalAssetPath(category: Category, id: String): String? {
        val assetManager = context?.assets ?: return null

        val candidateFolders = when (category) {
            Category.PEOPLE -> listOf("people", "characters")
            Category.FILMS -> listOf("films")
            Category.STARSHIPS -> listOf("starships", "vehicles", "hero")
            Category.PLANETS -> listOf("planets")
            Category.SPECIES -> listOf("species")
        }

        val extensions = listOf("jpg", "png", "jpeg", "webp")
        val hasKnownExtension = extensions.any { id.endsWith(".$it", ignoreCase = true) }

        for (folder in candidateFolders) {
            if (hasKnownExtension) {
                val assetPath = "$folder/$id"
                if (assetExists(assetManager, assetPath)) {
                    return assetPath
                }
            } else {
                if (assetExists(assetManager, "$folder/$id")) {
                    return "$folder/$id"
                }
                for (ext in extensions) {
                    val assetPath = "$folder/$id.$ext"
                    if (assetExists(assetManager, assetPath)) {
                        return assetPath
                    }
                }
            }
        }
        return null
    }

    private fun assetExists(assetManager: AssetManager, assetPath: String): Boolean {
        return try {
            assetManager.open(assetPath).use { true }
        } catch (_: Exception) {
            false
        }
    }

    private fun extractId(url: String): String? {
        val trimmed = url.trimEnd('/')
        val id = trimmed.substringAfterLast('/')
        return id.ifEmpty { null }
    }
}

typealias StarWarsImageResolverImpl = CharacterImageResolverImpl
