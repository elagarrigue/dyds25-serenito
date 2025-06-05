package edu.dyds.movies.di

import androidx.compose.runtime.Composable

import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.external.MoviesRemoteDataSourceImpl
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import edu.dyds.movies.presentation.home.HomeViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import edu.dyds.movies.data.local.MoviesLocalDataSourceImpl

private const val API_KEY = "d18da1b5da16397619c688b0263cd281"

object MoviesDependencyInjector {
    private val tmdbHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.themoviedb.org"
                parameters.append("api_key", API_KEY)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    private val remoteDataSource = MoviesRemoteDataSourceImpl(tmdbHttpClient)
    private val cache = MoviesLocalDataSourceImpl()
    private val repository = MoviesRepositoryImpl(remoteDataSource,cache)

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(GetPopularMoviesUseCase(repository))}
    }

    @Composable
    fun getDetailViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(GetMovieDetailUseCase(repository)) }
    }
}