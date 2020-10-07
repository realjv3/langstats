import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MainKtTest {

    val subj = LangStats()

    @Test
    fun main() {
        val posts = subj.getJobPostsByCity("Denver")

        val result = subj.calcLangShares(posts)
        assertTrue(result.size == 10)
    }
}