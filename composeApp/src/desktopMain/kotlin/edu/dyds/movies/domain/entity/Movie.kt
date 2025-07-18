package edu.dyds.movies.domain.entity

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val poster: String,
    val backdrop: String?,
    val originalTitle: String,
    val originalLanguage: String,
    val popularity: Double,
    val voteAverage: Double
)

data class QualifiedMovie(val movie: Movie, val isGoodMovie: Boolean)

