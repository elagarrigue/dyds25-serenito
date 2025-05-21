package edu.dyds.movies.domain.usecase
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie

//Intermediarios entre los ViewModel y Repository, este en particular se encarga de obtener los detalles del repositorio

class GetMovieDetailsUseCase(
    private val repository: MoviesRepository
){
    fun invokeMovieDetails(id: Int) : Movie? {
        return repository.getMovieDetails(id)
    }
}