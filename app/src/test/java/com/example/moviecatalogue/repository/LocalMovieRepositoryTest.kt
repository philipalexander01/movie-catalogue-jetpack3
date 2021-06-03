package com.example.moviecatalogue.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.example.moviecatalogue.favorite.database.LocalDataSource
import com.example.moviecatalogue.favorite.viewmodel.MovieViewModelTest
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.utils.DataDummy
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import java.util.concurrent.Executors

class LocalMovieRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private var local = mock(LocalDataSource::class.java)

    @Mock
    private lateinit var localMovieRepository: FakeLocalMovieRepository

    private val movieResponses = DataDummy.getDataMovie()
    private val movieResponse = DataDummy.getDataMovie()[0]
    private val movieId = movieResponses[0].id

    @Before
    fun setup() {
        localMovieRepository = FakeLocalMovieRepository(local)
    }


    @Test
    fun testGetAllMovie() {
        val movies = MutableLiveData<PagedList<Movie>>()
        movies.value =
            PagedTestDataSources.snapshot(MovieViewModelTest.PagedTestDataSources.snapshot(DataDummy.getDataMovie()))
        `when`(local.getMovie("movie")).thenReturn(movies)

        val data = localMovieRepository.getAllMovie("movie")
        assertEquals(data.value?.size, movies.value!!.size)

        verify(local).getMovie("movie")
        assertNotNull(data)
        assertEquals(movies.value!!.size, data.value?.size)
    }

    @Test
    fun testGetAllTv() {
        val tv = MutableLiveData<PagedList<Movie>>()
        tv.value =
            PagedTestDataSources.snapshot(MovieViewModelTest.PagedTestDataSources.snapshot(DataDummy.getDataTv()))
        `when`(local.getMovie("tv")).thenReturn(tv)

        val dataTv = localMovieRepository.getAllMovie("tv")
        verify(local).getMovie("tv")
        assertNotNull(dataTv)
        assertEquals(tv.value!!.size, dataTv.value?.size)

    }

    @Test
    fun testGetMovieById() {
        val movieData = MutableLiveData<Movie>()
        movieData.value = movieResponse

        `when`(local.getMovieById(movieId)).thenReturn(movieData)
        val detailEntity = localMovieRepository.getMovieById(movieId).value
        Assert.assertNotNull(detailEntity)
        assertEquals(movieResponse.id, detailEntity?.id)
        assertEquals(movieResponse.original_title, detailEntity?.original_title)
        assertEquals(movieResponse.overview, detailEntity?.overview)
        assertEquals(movieResponse.release_date, detailEntity?.release_date)
        assertEquals(movieResponse.poster_path, detailEntity?.poster_path)
        assertEquals(movieResponse.vote_average, detailEntity?.vote_average)
    }

    @Test
    fun testDelete() {
        val id = 1
        //delete data
        `when`(local.delete(id)).then { DataDummy.deleteDataMovie(id) }
        val dataMovie = DataDummy.getDataMovie()
        localMovieRepository.delete(id)
        dataMovie.minus(1)
        assertEquals(DataDummy.getDataMovie().size, dataMovie.size)
    }

    @Test
    fun testInsert() {
        val data = com.example.moviecatalogue.favorite.database.entity.Movie(
            movieResponse.id, null, movieResponse.original_title,
            movieResponse.overview, movieResponse.poster_path,
            movieResponse.release_date, null, movieResponse.vote_average, "movie"
        )
        localMovieRepository.insert(data)
        local.insert(data)
        verify(local).insert(data)
    }

    class PagedTestDataSources private constructor(private val items: List<Movie>) :
        PositionalDataSource<Movie>() {
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