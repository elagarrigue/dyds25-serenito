package edu.dyds.movies.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie

object TestDataFactory {

    fun createMovie(title: String): Movie {
        return Movie(
            id = title.hashCode(),
            title = title,
            overview = "Overview of $title",
            releaseDate = "2023-01-01",
            poster = "poster_${title.replace(" ", "_")}",
            backdrop = null,
            originalTitle = "Original $title",
            originalLanguage = "en",
            popularity = 10.0,
            voteAverage = 8.0
        )
    }

    fun createQualifiedMovie(title: String, isGood: Boolean = true): QualifiedMovie {
        return QualifiedMovie(
            movie = createMovie(title),
            isGoodMovie = isGood
        )
    }
}

