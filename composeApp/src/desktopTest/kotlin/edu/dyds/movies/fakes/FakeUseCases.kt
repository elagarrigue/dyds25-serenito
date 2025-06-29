package edu.dyds.movies.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase

class FakeSuccessDetailUseCase(
    private val movie: Movie = TestDataFactory.createMovie("Title 1")
) : GetMovieDetailUseCase {
    override suspend fun invoke(title: String): Movie? = movie
}

class FakeSuccessPopularMoviesUseCase(
    private val movies: List<QualifiedMovie>
) : GetPopularMoviesUseCase {
    override suspend fun invoke(): List<QualifiedMovie> = movies
}

