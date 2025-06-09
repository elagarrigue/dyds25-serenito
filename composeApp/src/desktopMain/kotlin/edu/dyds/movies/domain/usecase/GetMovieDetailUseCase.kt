package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie

interface GetMovieDetailUseCase{
    suspend fun invoke(id: Int): Movie?
}