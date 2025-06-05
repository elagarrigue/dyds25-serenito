@file:Suppress("FunctionName")

package edu.dyds.movies.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.dyds.movies.di.MoviesDependencyInjector
import edu.dyds.movies.presentation.detail.DetailScreen
import edu.dyds.movies.presentation.home.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = MoviesDependencyInjector.getHomeViewModel(),
                onGoodMovieClick = { movie ->
                    navController.navigate("detail/${movie.id}")
                }
            )
        }
        composable("detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            movieId?.let {
                DetailScreen(
                    viewModel = MoviesDependencyInjector.getDetailViewModel(),
                    movieId = it,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}