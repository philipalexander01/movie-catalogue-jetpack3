package com.example.moviecatalogue.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.example.moviecatalogue.favorite.repository.LocalMovieRepository
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.utils.DataDummy
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    private lateinit var movieViewModel: MovieViewModel

    @Mock
    private lateinit var localMovieRepository: LocalMovieRepository

    @Mock
    private lateinit var observer: Observer<PagedList<Movie>>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val movieEntities = DataDummy.getDataMovie()[0]

    @Before
    fun init() {
        movieViewModel = MovieViewModel(localMovieRepository)
    }

    @Test
    fun getDataMovie() {
        val movies = MutableLiveData<PagedList<Movie>>()
        movies.value = PagedTestDataSources.snapshot(PagedTestDataSources.snapshot(DataDummy.getDataMovie()))

        `when`(localMovieRepository.getAllMovie("movie")).thenReturn(movies)

        movieViewModel.getDataMovie("movie").observeForever(observer)
        Mockito.verify(observer).onChanged(movies.value)

        val expectedValue = movies.value
        val actualValue = movieViewModel.getDataMovie("movie").value
        assertEquals(expectedValue, actualValue)
        assertEquals(expectedValue?.snapshot(), actualValue?.snapshot())
        assertEquals(expectedValue?.size, actualValue?.size)
    }

    @Test
    fun getDataTv() {
        val movies = MutableLiveData<PagedList<Movie>>()
        movies.value = PagedTestDataSources.snapshot(PagedTestDataSources.snapshot(DataDummy.getDataTv()))
        `when`(localMovieRepository.getAllMovie("tv")).thenReturn(movies)

        movieViewModel.getDataMovie("tv").observeForever(observer)
        Mockito.verify(observer).onChanged(movies.value)

        val expectedValue = movies.value
        val actualValue = movieViewModel.getDataMovie("tv").value
        assertEquals(expectedValue, actualValue)
        assertEquals(expectedValue?.snapshot(), actualValue?.snapshot())
        assertEquals(expectedValue?.size, actualValue?.size)
    }

    @Test
    fun getDataMovieById() {
        val movieId = movieEntities.id
        val movieData = MutableLiveData<Movie>()
        movieData.value = movieEntities
        `when`(localMovieRepository.getMovieById(movieId)).thenReturn(movieData)
        val detailEntity = movieViewModel.getDataMovieById(movieId).value
        Mockito.verify(localMovieRepository).getMovieById(movieId)
        assertNotNull(detailEntity)
        TestCase.assertEquals(movieEntities.id, detailEntity?.id)
        TestCase.assertEquals(movieEntities.original_title, detailEntity?.original_title)
        TestCase.assertEquals(movieEntities.overview, detailEntity?.overview)
        TestCase.assertEquals(movieEntities.release_date, detailEntity?.release_date)
        TestCase.assertEquals(movieEntities.poster_path, detailEntity?.poster_path)
        TestCase.assertEquals(movieEntities.vote_average, detailEntity?.vote_average)
    }


    @Test
    fun insert() {
        val data = com.example.moviecatalogue.favorite.database.entity.Movie(
                movieEntities.id, null, movieEntities.original_title, movieEntities.overview, movieEntities.poster_path,
                movieEntities.release_date, null, movieEntities.vote_average, "movie"
        )
        movieViewModel.insert(data)
        verify(localMovieRepository).insert(data)
    }

    @Test
    fun delete() {
        val id = 1
        val movieData = MutableLiveData<PagedList<Movie>>()
        movieData.value = PagedTestDataSources.snapshot(DataDummy.getDataMovie())
        `when`(localMovieRepository.getAllMovie("movie")).thenReturn(movieData)

        `when`(localMovieRepository.delete(id)).then { DataDummy.deleteDataMovie(id) }
        var dataSize = movieViewModel.getDataMovie("movie").value?.size
        movieViewModel.delete(id)
        dataSize = dataSize?.minus(1)

        val movies = MutableLiveData<PagedList<Movie>>()
        movies.value = PagedTestDataSources.snapshot(PagedTestDataSources.snapshot(DataDummy.getDataMovie()))
        `when`(localMovieRepository.getAllMovie("movie")).thenReturn(movies)
        val dataSize1 = movieViewModel.getDataMovie("movie").value?.size
        assertEquals(dataSize,dataSize1)

    }

    class PagedTestDataSources private constructor(private val items: List<Movie>) : PositionalDataSource<Movie>() {
        companion object {
            fun snapshot(items: List<Movie> = listOf()): PagedList<Movie> {
                return PagedList.Builder(PagedTestDataSources(items), 5)
                        .setNotifyExecutor(Executors.newSingleThreadExecutor())
                        .setFetchExecutor(Executors.newSingleThreadExecutor())
                        .build()
            }
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
            callback.onResult(items, 0, items.size)
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
            val start = params.startPosition
            val end = params.startPosition + params.loadSize
            callback.onResult(items.subList(start, end))
        }
    }
}