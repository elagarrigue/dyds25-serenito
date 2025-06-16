package utils

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie

object TestDataFactory {

    fun createMovie(id: Int): Movie {
        return Movie(
            id = id,
            title = "Title $id",
            overview = "Overview $id",
            releaseDate = "2023-01-01",
            poster = "poster_$id",
            backdrop = null,
            originalTitle = "Original Title $id",
            originalLanguage = "en",
            popularity = 10.0,
            voteAverage = 8.0
        )
    }

    fun createQualifiedMovie(id: Int, isGood: Boolean = true): QualifiedMovie {
        return QualifiedMovie(
            movie = createMovie(id),
            isGoodMovie = isGood
        )
    }
}
