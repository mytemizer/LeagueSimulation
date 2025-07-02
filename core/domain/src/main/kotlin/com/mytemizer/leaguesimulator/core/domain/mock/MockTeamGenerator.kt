package com.mytemizer.leaguesimulator.core.domain.mock

import com.mytemizer.leaguesimulator.core.domain.model.Team
import java.util.UUID
import kotlin.random.Random

/**
 * Generator for creating mock teams for testing and demo purposes
 */
object MockTeamGenerator {

    private val teamNames = listOf(
        // Premier League inspired
        "Manchester United" to "MUN",
        "Liverpool FC" to "LIV",
        "Arsenal FC" to "ARS",
        "Chelsea FC" to "CHE",
        "Manchester City" to "MCI",
        "Tottenham Hotspur" to "TOT",
        "Newcastle United" to "NEW",
        "Brighton & Hove" to "BHA",
        "West Ham United" to "WHU",
        "Aston Villa" to "AVL",

        // La Liga inspired
        "Real Madrid" to "RMA",
        "FC Barcelona" to "BAR",
        "Atletico Madrid" to "ATM",
        "Sevilla FC" to "SEV",
        "Valencia CF" to "VAL",
        "Real Sociedad" to "RSO",
        "Athletic Bilbao" to "ATH",
        "Villarreal CF" to "VIL",
        "Real Betis" to "BET",
        "Celta Vigo" to "CEL",

        // Serie A inspired
        "Juventus FC" to "JUV",
        "AC Milan" to "MIL",
        "Inter Milan" to "INT",
        "AS Roma" to "ROM",
        "SSC Napoli" to "NAP",
        "Atalanta BC" to "ATA",
        "Lazio SS" to "LAZ",
        "Fiorentina" to "FIO",
        "Torino FC" to "TOR",
        "Bologna FC" to "BOL",

        // Bundesliga inspired
        "Bayern Munich" to "BAY",
        "Borussia Dortmund" to "BVB",
        "RB Leipzig" to "RBL",
        "Bayer Leverkusen" to "B04",
        "Eintracht Frankfurt" to "SGE",
        "VfL Wolfsburg" to "WOB",
        "SC Freiburg" to "SCF",
        "Borussia M'gladbach" to "BMG",
        "FC Union Berlin" to "FCU",
        "VfB Stuttgart" to "VFB",

        // Ligue 1 inspired
        "Paris Saint-Germain" to "PSG",
        "Olympique Marseille" to "OM",
        "AS Monaco" to "ASM",
        "Olympique Lyon" to "OL",
        "OGC Nice" to "NIC",
        "Stade Rennais" to "REN",
        "RC Lens" to "RCL",
        "Lille OSC" to "LIL",
        "Nantes FC" to "FCN",
        "Montpellier HSC" to "MON"
    )

    private val cities = listOf(
        "Manchester", "Liverpool", "London", "Birmingham", "Newcastle", "Brighton",
        "Madrid", "Barcelona", "Seville", "Valencia", "San Sebastian", "Bilbao",
        "Turin", "Milan", "Rome", "Naples", "Bergamo", "Florence",
        "Munich", "Dortmund", "Leipzig", "Leverkusen", "Frankfurt", "Wolfsburg",
        "Paris", "Marseille", "Monaco", "Lyon", "Nice", "Rennes"
    )

    private val stadiums = listOf(
        "Old Trafford", "Anfield", "Emirates Stadium", "Stamford Bridge", "Etihad Stadium",
        "Santiago Bernabeu", "Camp Nou", "Wanda Metropolitano", "Ramon Sanchez Pizjuan",
        "Allianz Stadium", "San Siro", "Stadio Olimpico", "Stadio San Paolo",
        "Allianz Arena", "Signal Iduna Park", "Red Bull Arena", "BayArena",
        "Parc des Princes", "Orange Velodrome", "Stade Louis II", "Groupama Stadium"
    )

    private val primaryColors = listOf(
        "#FF0000", // Red (Manchester United, Liverpool)
        "#0000FF", // Blue (Chelsea, Manchester City)
        "#FFFFFF", // White (Real Madrid, Tottenham)
        "#000000", // Black (Newcastle, Juventus)
        "#00FF00", // Green (Celtic)
        "#FFFF00", // Yellow (Borussia Dortmund)
        "#800080", // Purple (Fiorentina)
        "#FFA500", // Orange (Netherlands, Valencia)
        "#008080", // Teal
        "#800000", // Maroon (West Ham)
        "#DC143C", // Crimson (Bayern Munich)
        "#4169E1", // Royal Blue (Leicester City)
        "#228B22", // Forest Green
        "#FF6347", // Tomato Red
        "#1E90FF", // Dodger Blue
        "#32CD32", // Lime Green
        "#FF4500", // Orange Red
        "#9932CC", // Dark Orchid
        "#2E8B57", // Sea Green
        "#B22222", // Fire Brick
        "#4682B4", // Steel Blue
        "#DAA520", // Goldenrod
        "#8B0000", // Dark Red
        "#191970", // Midnight Blue
        "#006400", // Dark Green
        "#FF1493", // Deep Pink
        "#00CED1", // Dark Turquoise
        "#FF8C00", // Dark Orange
        "#9400D3", // Violet
        "#556B2F"  // Dark Olive Green
    )

    private val secondaryColors = listOf(
        "#FFFFFF", // White (most common secondary)
        "#000000", // Black (classic secondary)
        "#C0C0C0", // Silver
        "#FFD700", // Gold (prestigious clubs)
        "#FF0000", // Red
        "#0000FF", // Blue
        "#008000", // Green
        "#FFFF00", // Yellow
        "#F5F5DC", // Beige
        "#708090", // Slate Gray
        "#2F4F4F", // Dark Slate Gray
        "#8FBC8F", // Dark Sea Green
        "#CD853F", // Peru
        "#D2691E", // Chocolate
        "#B8860B", // Dark Goldenrod
        "#A0522D", // Sienna
        "#696969", // Dim Gray
        "#778899", // Light Slate Gray
        "#483D8B", // Dark Slate Blue
        "#2E8B57", // Sea Green
        "#8B4513", // Saddle Brown
        "#4682B4", // Steel Blue
        "#D3D3D3", // Light Gray
        "#A9A9A9", // Dark Gray
        "#87CEEB", // Sky Blue
        "#98FB98", // Pale Green
        "#F0E68C", // Khaki
        "#DDA0DD", // Plum
        "#20B2AA", // Light Sea Green
        "#87CEFA"  // Light Sky Blue
    )

    /**
     * Generate a list of mock teams
     */
    fun generateTeams(count: Int): List<Team> {
        require(count > 0) { "Count must be positive" }
        require(count <= teamNames.size) { "Cannot generate more than ${teamNames.size} unique teams" }

        val shuffledTeams = teamNames.shuffled().take(count)
        val shuffledCities = cities.shuffled()
        val shuffledStadiums = stadiums.shuffled()

        return shuffledTeams.mapIndexed { index, (name, shortName) ->
            generateTeam(
                name = name,
                shortName = shortName,
                city = shuffledCities[index % shuffledCities.size],
                stadium = shuffledStadiums[index % shuffledStadiums.size],
            )
        }
    }

    /**
     * Generate a single team with realistic ratings
     */
    fun generateTeam(
        name: String,
        shortName: String,
        city: String,
        stadium: String,
    ): Team {
        // Generate base rating (40-95 range for realism)
        val baseRating = Random.nextInt(40, 96)

        // Generate individual ratings around the base rating with some variance
        val attack = generateRatingAroundBase(baseRating, variance = 15)
        val midfield = generateRatingAroundBase(baseRating, variance = 10)
        val defense = generateRatingAroundBase(baseRating, variance = 12)
        val goalkeeper = generateRatingAroundBase(baseRating, variance = 20)

        // Calculate overall rating as weighted average
        val overallRating = ((attack * 0.3 + midfield * 0.25 + defense * 0.25 + goalkeeper * 0.2).toInt())
            .coerceIn(30, 99)

        return Team(
            id = UUID.randomUUID().mostSignificantBits, // Will be set by database
            name = name,
            shortName = shortName,
            logoUrl = null, // Could be added later
            foundedYear = Random.nextInt(1880, 2000),
            stadium = stadium,
            city = city,
            country = "England", // Default for now
            primaryColor = primaryColors.random(),
            secondaryColor = secondaryColors.random(),
            overallRating = overallRating,
            attack = attack,
            midfield = midfield,
            defense = defense,
            goalkeeper = goalkeeper,
            isUserTeam = false
        )
    }

    /**
     * Generate a world class team (85+ overall)
     */
    fun generateWorldClassTeam(): Team {
        val (name, shortName) = teamNames.random()
        val city = cities.random()
        val stadium = stadiums.random()

        // Generate team with target overall rating of 85-95
        val targetOverall = Random.nextInt(85, 96)
        return generateTeamWithTargetRating(
            name = name,
            shortName = shortName,
            city = city,
            stadium = stadium,
            targetRating = targetOverall,
        )
    }

    /**
     * Generate an excellent team (75-84 overall)
     */
    fun generateExcellentTeam(): Team {
        val (name, shortName) = teamNames.random()
        val city = cities.random()
        val stadium = stadiums.random()

        // Generate team with target overall rating of 75-84
        val targetOverall = Random.nextInt(75, 85)
        return generateTeamWithTargetRating(
            name = name,
            shortName = shortName,
            city = city,
            stadium = stadium,
            targetRating = targetOverall,
        )
    }

    /**
     * Generate a good team (65-74 overall)
     */
    fun generateGoodTeam(): Team {
        val (name, shortName) = teamNames.random()
        val city = cities.random()
        val stadium = stadiums.random()

        // Generate team with target overall rating of 65-74
        val targetOverall = Random.nextInt(65, 75)
        return generateTeamWithTargetRating(
            name = name,
            shortName = shortName,
            city = city,
            stadium = stadium,
            targetRating = targetOverall,
        )
    }

    /**
     * Generate a team with a specific target overall rating
     */
    private fun generateTeamWithTargetRating(
        name: String,
        shortName: String,
        city: String,
        stadium: String,
        targetRating: Int,
    ): Team {
        // Generate individual ratings around the target with some variance
        val attack = generateRatingAroundBase(targetRating, variance = 8)
        val midfield = generateRatingAroundBase(targetRating, variance = 8)
        val defense = generateRatingAroundBase(targetRating, variance = 8)
        val goalkeeper = generateRatingAroundBase(targetRating, variance = 10)

        // Calculate the actual overall rating from individual stats
        val overallRating = (attack * 0.3 + midfield * 0.25 + defense * 0.25 + goalkeeper * 0.2).toInt()

        return Team(
            id = 0,
            name = name,
            shortName = shortName,
            logoUrl = null,
            foundedYear = Random.nextInt(1880, 2000),
            stadium = stadium,
            city = city,
            country = "England",
            primaryColor = primaryColors.random(),
            secondaryColor = secondaryColors.random(),
            overallRating = overallRating,
            attack = attack,
            midfield = midfield,
            defense = defense,
            goalkeeper = goalkeeper,
            isUserTeam = false
        )
    }

    private fun generateRatingAroundBase(base: Int, variance: Int): Int {
        val min = (base - variance).coerceAtLeast(30)
        val max = (base + variance).coerceAtMost(99)
        return Random.nextInt(min, max + 1)
    }

}
