package utils

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase

class FakeSuccessDetailUseCase(
    private val movie: Movie = TestDataFactory.createMovie(42)
) : GetMovieDetailUseCase {
    override suspend fun invoke(id: Int): Movie? = movie
}

class FakeFailingDetailUseCase : GetMovieDetailUseCase {
    override suspend fun invoke(id: Int): Movie? {
        throw RuntimeException("Simulated failure")
    }
}

class FakeSuccessPopularMoviesUseCase(
    private val movies: List<QualifiedMovie>
) : GetPopularMoviesUseCase {
    override suspend fun invoke(): List<QualifiedMovie> = movies
}

class FakeFailingPopularMoviesUseCase : GetPopularMoviesUseCase {
    override suspend fun invoke(): List<QualifiedMovie> {
        throw RuntimeException("Simulated failure")
    }
}
