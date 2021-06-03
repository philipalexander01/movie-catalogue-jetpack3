package com.example.moviecatalogue.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.DetailActivity
import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.MoviePagedListAdapter
import com.example.moviecatalogue.adapter.MovieViewAdapter
import com.example.moviecatalogue.databinding.FragmentMovieBinding
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.viewmodel.MovieViewModel
import com.example.moviecatalogue.viewmodel.TvViewModel
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieFragment : Fragment() {
    private lateinit var movieFavoriteViewModel: com.example.moviecatalogue.favorite.viewmodel.MovieViewModel
    private var typee = ""
    private val movieViewAdapter = MovieViewAdapter()
    private val factory = ViewModelFactory.getInstance()

    private lateinit var adapter: MoviePagedListAdapter

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val PAGE = "page"

        @JvmStatic
        fun newInstance(index: Int, page: String) =
            MovieFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(PAGE, page)
                }
            }
    }

    private var binding: FragmentMovieBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MoviePagedListAdapter()
        movieFavoriteViewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val page = arguments?.getString(PAGE)

        binding?.apply {
            pbLoading.visibility = View.VISIBLE
            rvMovie.layoutManager = LinearLayoutManager(requireActivity())
            rvMovie.adapter = movieViewAdapter
        }
        if (index == 1) {
            if (page == getString(R.string.home)) {
                getMovieData()
            } else if (page == getString(R.string.fav)) {
                getFavMovie(getString(R.string.movie))
                typee = getString(R.string.movie)
            }

        } else if (index == 2) {
            if (page == getString(R.string.home)) {
              getTvData()
            } else if (page == getString(R.string.fav)) {
                getFavMovie(getString(R.string.tv))
                typee = getString(R.string.tv)
            }

        }


        movieViewAdapter.setOnItemClickCallback(object : MovieViewAdapter.OnItemClickCallback {
            override fun onItemClick(data: Movie) {
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(getString(R.string.id), data.id)
                    putExtra(getString(R.string.type), typee)
                }
                startActivity(intent)
            }
        })


        adapter.setOnItemClickCallback(object : MoviePagedListAdapter.OnItemClickCallback {
            override fun onItemClick(data: Movie) {
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(getString(R.string.id), data.id)
                    putExtra(getString(R.string.type), typee)
                }
                startActivity(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): com.example.moviecatalogue.favorite.viewmodel.MovieViewModel {
        val factory =
            com.example.moviecatalogue.favorite.viewmodel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            activity,
            factory
        ).get(com.example.moviecatalogue.favorite.viewmodel.MovieViewModel::class.java)
    }

    private fun getFavMovie(type1: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = movieFavoriteViewModel.getDataMovie(type1)
            withContext(Dispatchers.Main){
                movie.observe(requireActivity(), {
                if (it.isNotEmpty()) {
                    adapter.submitList(it)
                    binding?.apply {
                        rvMovie.layoutManager = LinearLayoutManager(requireActivity())
                        rvMovie.adapter = adapter
                    }
//                movieViewAdapter.setData(it as ArrayList<Movie>)
                } else {
                    Toast.makeText(
                            requireActivity(),
                            getString(R.string.data_not_exist),
                            Toast.LENGTH_SHORT
                    ).show()
                }
                binding?.pbLoading?.visibility = View.GONE
            })
            }


        }
    }

    private fun getMovieData() {
        val movieViewModel =
            ViewModelProvider(requireActivity(), factory)[MovieViewModel::class.java]
        movieViewModel.getDataMovie().observe(requireActivity(), {
            if (it.isNotEmpty()) {
                movieViewAdapter.setData(it)
                typee = getString(R.string.movie)
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.data_not_exist),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding?.pbLoading?.visibility = View.GONE
        })
    }
    private fun getTvData(){
        val tvViewModel =
            ViewModelProvider(requireActivity(), factory)[TvViewModel::class.java]
        tvViewModel.getDataTv().observe(requireActivity(), {
            if (it.isNotEmpty()) {
                movieViewAdapter.setData(it)
                typee = getString(R.string.tv)
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.data_not_exist),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding?.pbLoading?.visibility = View.GONE
        })
    }
}