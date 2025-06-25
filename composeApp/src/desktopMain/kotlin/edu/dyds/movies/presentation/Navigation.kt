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
                    navController.navigate("detail/${movie.title}")
                }
            )
        }
        composable("detail/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            title?.let {
                DetailScreen(
                    viewModel = MoviesDependencyInjector.getDetailViewModel(),
                    title = it,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}