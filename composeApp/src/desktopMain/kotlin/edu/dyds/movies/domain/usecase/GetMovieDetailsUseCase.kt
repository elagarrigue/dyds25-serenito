package edu.dyds.movies.domain.usecase
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie

class GetMovieDetailUseCase(
    private val repository: MoviesRepository
) {
    suspend fun invokeMovieDetails(id: Int): Movie? {
        return repository.getMovieDetails(id)
    }
}