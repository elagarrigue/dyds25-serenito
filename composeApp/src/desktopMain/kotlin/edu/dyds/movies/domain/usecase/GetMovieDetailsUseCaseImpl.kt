package edu.dyds.movies.domain.usecase
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie

class GetMovieDetailUseCaseImpl(
    private val repository: MoviesRepository
) : GetMovieDetailUseCase {
    override suspend fun invoke(title: String): Movie? {
        return repository.getMovieDetails(title)
    }
}