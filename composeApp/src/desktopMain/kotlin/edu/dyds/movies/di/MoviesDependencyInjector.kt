package edu.dyds.movies.di

import MoviesRemoteDataSourceImpl
import androidx.compose.runtime.Composable

import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.broker.MoviesDetailBroker
import edu.dyds.movies.data.external.omdb.OMDBMoviesDetailSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesDetailSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesListSource
import edu.dyds.movies.presentation.detail.DetailViewModel
import edu.dyds.movies.presentation.home.HomeViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import edu.dyds.movies.data.local.MoviesLocalDataSourceImpl
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCaseImpl
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl

object MoviesDependencyInjector {
    private const val TMDB_API_KEY = "d18da1b5da16397619c688b0263cd281"
    private const val OMDB_API_KEY = "a6b87474"

    private val tmdbHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.themoviedb.org"
                parameters.append("api_key", TMDB_API_KEY)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    private val omdbHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "www.omdbapi.com"
                parameters.append("apikey", OMDB_API_KEY)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    private val tmdbListSource = TMDBMoviesListSource(tmdbHttpClient)
    private val tmdbDetailSource = TMDBMoviesDetailSource(tmdbHttpClient)
    private val omdbDetailSource = OMDBMoviesDetailSource(omdbHttpClient)

    private val moviesDetailBroker = MoviesDetailBroker(tmdbDetailSource, omdbDetailSource)
    private val moviesRemoteDataSource = MoviesRemoteDataSourceImpl(
        moviesListSource = tmdbListSource,
        moviesDetailSource = moviesDetailBroker
    )

    private val moviesLocalDataSource = MoviesLocalDataSourceImpl()
    private val repository = MoviesRepositoryImpl(moviesRemoteDataSource, moviesLocalDataSource)

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(GetPopularMoviesUseCaseImpl(repository)) }
    }

    @Composable
    fun getDetailViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(GetMovieDetailUseCaseImpl(repository)) }
    }


}