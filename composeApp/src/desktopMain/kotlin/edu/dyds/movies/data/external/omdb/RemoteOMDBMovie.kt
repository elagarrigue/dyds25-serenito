package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOMDBMovie(
    @SerialName("Title") val title: String,
    @SerialName("Plot") val plot: String,
    @SerialName("Released") val releaseDate: String,
    @SerialName("Poster") val posterUrl: String,
    @SerialName("Language") val language: String,
    @SerialName("imdbRating") val voteAverageStr: String
) {
    fun toDomainMovie(): Movie{
        return Movie(
            id = 0,
            title = title,
            overview = plot,
            releaseDate = releaseDate,
            poster = posterUrl,
            backdrop = null,
            originalTitle = title,
            originalLanguage = language,
            popularity = 0.0,
            voteAverage = voteAverageStr.toDoubleOrNull() ?: 0.0
        )
    }
}
