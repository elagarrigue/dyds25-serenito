package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.QualifiedMovie

interface GetPopularMoviesUseCase{
    suspend fun invoke(): List<QualifiedMovie>
}