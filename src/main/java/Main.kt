import java.net.URL
import java.net.URLEncoder
import kotlinx.serialization.*
import kotlinx.serialization.json.*


@Serializable
data class JobPost(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val description: String,
)

class LangStats {

    val cities = listOf(
            "Boston",
            "San Francisco",
            "Los Angeles",
            "Denver",
            "Boulder",
            "Chicago",
            "New York",
            "Raleigh",
    )
    private val format = Json { ignoreUnknownKeys = true }

    /**
     * Returns body of an HTTP GET request to passed url
     */
    fun doGet(url: String): String = URL(url).readText()

    /**
     * Hits github jobs API for job ads by location
     */
    fun getJobPostsByCity(city: String): List<JobPost> {
        var jobPosts = listOf<JsonElement>()
        var i = 0
        do {
            val loc = URLEncoder.encode(city).toLowerCase()
            val resp = doGet("https://jobs.github.com/positions.json?location=$loc&page=$i")
            val page = Json.parseToJsonElement(resp).jsonArray
            jobPosts += page
            i++
        } while (page.size == 50)

        return jobPosts.map { format.decodeFromJsonElement<JobPost>(it) }
    }

    /**
     * Calculates backend programming language breakdown of passed job posts
     */
    fun calcLangShares(jobPosts: List<JobPost>): List<Pair<String, String>> {

        val langCounts = mutableMapOf(
                "Java" to 0,
                "C#" to 0,
                "Python" to 0,
                "Swift" to 0,
                "Objective-C" to 0,
                "Ruby" to 0,
                "Kotlin" to 0,
                "Go" to 0,
                "C++" to 0,
                "Scala" to 0,
        )
        jobPosts.forEach { job ->

            langCounts.forEach { lang ->
                // If language is found in job description, increment languages' count
                val regx = "[\\s\\W]+${ if (lang.key == "C++") "C\\+\\+" else lang.key }[\\s\\W]+"

                if (Regex(regx, RegexOption.IGNORE_CASE).find(job.description) != null) {

                    langCounts[lang.key] = langCounts[lang.key]?.plus(1) ?: 0
                }
            }
        }
        return langCounts.map { lang ->
            val numJobs = langCounts[lang.key]!!
            lang.key to "${if (numJobs > 0) (100 / numJobs) else 0}%"
        }
    }
}

fun main() {
    val langStats = LangStats()

    langStats.cities.forEach { city ->

        println("$city:")
        val jobPosts = langStats.getJobPostsByCity(city)

        if (jobPosts.isNotEmpty()) {
            val langShares = langStats.calcLangShares(jobPosts)
            langShares.forEach { lang ->
                println("    -${lang.first}: ${lang.second}")
            }
            println("")
        } else {
            println("    Sadly, there are no jobs posted in $city.")
            println("")
        }
    }
}