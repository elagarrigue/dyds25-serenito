import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class FakeMoviesRepository : MoviesRepository {
    var movieToReturn: Movie? = null
    var lastTitleRequested: String? = null
    var popularMoviesToReturn: List<Movie> = emptyList()
    var getPopularMoviesCalled = false

    override suspend fun getMovieDetails(title: String): Movie? {
        lastTitleRequested = title
        return movieToReturn
    }

    override suspend fun getPopularMovies(): List<Movie> {
        getPopularMoviesCalled = true
        return popularMoviesToReturn
    }
}
