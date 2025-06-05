package edu.dyds.movies.domain.usecase
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie

const val MIN_VOTE_AVERAGE = 6.0

class GetPopularMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend fun invokePopularMovies(): List<QualifiedMovie> {
        return repository.getPopularMovies().sortAndMap()
    }

    private fun List<Movie>.sortAndMap(): List<QualifiedMovie> {
        return this
            .sortedByDescending { it.voteAverage }
            .map {
                QualifiedMovie(
                    movie = it,
                    isGoodMovie = it.voteAverage >= MIN_VOTE_AVERAGE
                )
            }
    }
}