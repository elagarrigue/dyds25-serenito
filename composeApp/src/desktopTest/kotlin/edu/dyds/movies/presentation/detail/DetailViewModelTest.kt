package edu.dyds.movies.presentation.detail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import edu.dyds.movies.fakes.FakeSuccessDetailUseCase
import edu.dyds.movies.fakes.TestDataFactory

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @Test
    fun `initial state must be empty and not loading`() {
        val viewModel = DetailViewModel(FakeSuccessDetailUseCase())
        val state = viewModel.state.value
        Assert.assertTrue(state.isLoading)
        Assert.assertNull(state.movie)
    }

    @Test
    fun `when getMovieDetail succeeds, the state is updated with the movie`() = runTest {
        val movie = TestDataFactory.createMovie(1)
        val viewModel = DetailViewModel(FakeSuccessDetailUseCase(movie))
        viewModel.getMovieDetail(1)
        val state = viewModel.state.first { !it.isLoading }
        Assert.assertEquals(movie, state.movie)
    }

}