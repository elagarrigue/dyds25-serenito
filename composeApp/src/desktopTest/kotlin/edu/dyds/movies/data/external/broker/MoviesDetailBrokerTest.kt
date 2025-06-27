package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource
import edu.dyds.movies.domain.entity.Movie
import org.junit.Assert
import org.junit.Test

class MoviesDetailBrokerTest {

    class FakeSource(private val movieToReturn: Movie?) : MoviesDetailExternalSource {
        override suspend fun getMovieByTitle(title: String): Movie? {
            return if (movieToReturn?.title == title) movieToReturn else null
        }
    }

    private val tmdbMovie = Movie(
        id = 1,
        title = "Inception",
        overview = "TMDB Overview",
        releaseDate = "2010-07-16",
        poster = "tmdb-poster.jpg",
        backdrop = "tmdb-backdrop.jpg",
        originalTitle = "Inception",
        originalLanguage = "en",
        popularity = 80.0,
        voteAverage = 8.5
    )

    private val omdbMovie = Movie(
        id = 1,
        title = "Inception",
        overview = "OMDB Overview",
        releaseDate = "2010-07-16",
        poster = "omdb-poster.jpg",
        backdrop = "omdb-backdrop.jpg",
        originalTitle = "Inception",
        originalLanguage = "en",
        popularity = 100.0,
        voteAverage = 9.1
    )

    @Test
    fun `should combine movies from both sources when both return a result`() {
        val broker = MoviesDetailBroker(
            tmdbSource = FakeSource(tmdbMovie),
            omdbSource = FakeSource(omdbMovie)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Inception")
        }

        Assert.assertEquals("Inception", result?.title)
        Assert.assertEquals(
            "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
            result?.overview
        )
        Assert.assertEquals(90.0, result?.popularity)
        Assert.assertEquals(8.8, result?.voteAverage)
    }

    @Test
    fun `should return only TMDB movie when OMDB returns null`() {
        val broker = MoviesDetailBroker(
            tmdbSource = FakeSource(tmdbMovie),
            omdbSource = FakeSource(null)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Inception")
        }

        Assert.assertEquals("Inception", result?.title)
        Assert.assertEquals("TMDB: ${tmdbMovie.overview}", result?.overview)
    }

    @Test
    fun `should return only OMDB movie when TMDB returns null`() {
        val broker = MoviesDetailBroker(
            tmdbSource = FakeSource(null),
            omdbSource = FakeSource(omdbMovie)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Inception")
        }

        Assert.assertEquals("Inception", result?.title)
        Assert.assertEquals("OMDB: ${omdbMovie.overview}", result?.overview)
    }

    @Test
    fun `should return null when both sources return null`() {
        val broker = MoviesDetailBroker(
            tmdbSource = FakeSource(null),
            omdbSource = FakeSource(null)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Inception")
        }

        Assert.assertNull(result)
    }
}
