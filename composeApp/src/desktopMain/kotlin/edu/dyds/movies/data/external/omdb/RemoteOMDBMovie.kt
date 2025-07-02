package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOMDBMovie(
    @SerialName("Response") val response: String,
    @SerialName("Error") val error: String? = null,
    @SerialName("Title") val title: String? = null,
    @SerialName("Plot") val plot: String? = null,
    @SerialName("imdbRating") val imdbRating: String? = null
) {
    fun isSuccess() = response.equals("True", ignoreCase = true)

    fun toDomainMovie(): Movie? {
        if (!isSuccess()) return null

        return Movie(
            id = 0,
            title = title ?: "Título desconocido",
            overview = plot ?: "Sin descripción",
            releaseDate = "Fecha desconocida",
            poster = "",
            backdrop = null,
            originalTitle = title ?: "Título desconocido",
            originalLanguage = "N/A",
            popularity = 0.0,
            voteAverage = imdbRating?.toDoubleOrNull() ?: 0.0
        )
    }
}
