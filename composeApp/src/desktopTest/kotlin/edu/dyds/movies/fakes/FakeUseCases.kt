package edu.dyds.movies.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase

class FakeSuccessDetailUseCase(
    private val movie: Movie = TestDataFactory.createMovie(42)
) : GetMovieDetailUseCase {
    override suspend fun invoke(id: Int): Movie? = movie
}

class FakeSuccessPopularMoviesUseCase(
    private val movies: List<QualifiedMovie>
) : GetPopularMoviesUseCase {
    override suspend fun invoke(): List<QualifiedMovie> = movies
}

