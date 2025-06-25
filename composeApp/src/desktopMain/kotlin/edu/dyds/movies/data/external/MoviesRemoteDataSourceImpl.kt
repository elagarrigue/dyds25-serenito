import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource
import edu.dyds.movies.data.external.contracts.MoviesListExternalSource
import edu.dyds.movies.data.external.MoviesExternalDataSource
import edu.dyds.movies.domain.entity.Movie

class MoviesRemoteDataSourceImpl(
    private val moviesListSource: MoviesListExternalSource,
    private val moviesDetailSource: MoviesDetailExternalSource
) : MoviesExternalDataSource {
    override suspend fun getPopularMovies(): List<Movie> {
        return moviesListSource.getPopularMovies()
    }

    override suspend fun getMovieDetails(title: String): Movie? {
        return moviesDetailSource.getMovieByTitle(title)
    }
}