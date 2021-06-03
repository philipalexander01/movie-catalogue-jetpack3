package com.example.moviecatalogue

import android.content.Intent
import android.graphics.Color
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviecatalogue.databinding.ActivityDetailBinding
import com.example.moviecatalogue.databinding.NoInternetLayoutBinding
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.utils.Utility
import com.example.moviecatalogue.viewmodel.MovieViewModel
import com.example.moviecatalogue.viewmodel.TvViewModel
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty

class DetailActivity : AppCompatActivity() {
    private var binding: ActivityDetailBinding? = null
    private var binding1: NoInternetLayoutBinding? = null
    private var actNw: NetworkCapabilities? = null
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val data: ArrayList<Movie> = ArrayList()
        val factory = ViewModelFactory.getInstance()

        val id = intent.getIntExtra(this.getString(R.string.id), 0)
        val type = intent.getStringExtra(this.getString(R.string.type))
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        val movieFavoriteViewModel = obtainViewModel(this@DetailActivity)
        //check internet connection
        actNw = Utility().checkInternetConnection(this)
        //if there is no an internet connection
        if (actNw == null) {
            binding1 = NoInternetLayoutBinding.inflate(layoutInflater)
            setContentView(binding1?.root)

            binding1?.btnCobaLagi?.setOnClickListener {
                actNw = Utility().checkInternetConnection(this)
                if (actNw != null) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(getString(R.string.id), id)
                    intent.putExtra(getString(R.string.type), type)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            binding?.pbLoading?.visibility = View.VISIBLE
            var isActive = false
            when (type) {
                getString(R.string.movie) -> {
                    val movieViewModel =
                        ViewModelProvider(this, factory)[MovieViewModel::class.java]

                    movieViewModel.getDataMovieById(id).observe(this, {
                        if (it != null) {
                            from = getString(R.string.movie)
                            data.clear()
                            data.add(it)
                            setData(it)
                            movieFavoriteViewModel.getDataMovieById(it.id).observe(this, {
                                if (it != null) {
                                    isActive = true
                                    setStatus(isActive)
                                }
                            })
                        } else {
                            Toast.makeText(
                                this@DetailActivity,
                                getString(R.string.data_not_exist),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        binding?.pbLoading?.visibility = View.GONE
                    })
                }
                getString(R.string.tv) -> {
                    val tvViewModel = ViewModelProvider(this, factory)[TvViewModel::class.java]
                    tvViewModel.getDataTvById(id).observe(this, {
                        if (it != null) {
                            from=getString(R.string.tv)
                            data.clear()
                            data.add(it)
                            setData(it)
                            movieFavoriteViewModel.getDataMovieById(it.id).observe(this, {
                                if (it != null) {
                                    isActive = true
                                    setStatus(isActive)
                                }
                            })
                        } else {
                            Toast.makeText(
                                this@DetailActivity,
                                getString(R.string.data_not_exist)
                                ,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        binding?.pbLoading?.visibility = View.GONE
                    })
                }
            }

            binding?.favButton?.setOnClickListener {
                //if already add to favorite
                if (isActive) {
                    isActive = false
                    setStatus(isActive)

                    //delete data
                    data[0].let { it1 -> movieFavoriteViewModel.delete(it1.id) }
                    Toasty.success(
                        this@DetailActivity,
                        getString(R.string.sukses),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                } else {
                    isActive = true
                    setStatus(isActive)
                    //insert data to database
                    if(from == getString(R.string.movie)){
                        movieFavoriteViewModel.insert(
                            com.example.moviecatalogue.favorite.database.entity.Movie(
                                id = data[0].id,
                                original_title = data[0].original_title,
                                overview = data[0].overview,
                                poster_path = data[0].poster_path,
                                vote_average = data[0].vote_average,
                                release_date = data[0].release_date,
                                type = getString(R.string.movie)
                            )
                        )
                    }else{
                        movieFavoriteViewModel.insert(
                            com.example.moviecatalogue.favorite.database.entity.Movie(
                                id = data[0].id,
                                original_name = data[0].original_name,
                                overview = data[0].overview,
                                poster_path = data[0].poster_path,
                                vote_average = data[0].vote_average,
                                first_air_date = data[0].first_air_date,
                                type = getString(R.string.tv)
                            )
                        )
                    }

                    Toasty.success(
                        this@DetailActivity,
                        getString(R.string.sukses1),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }
        }
    }

    private fun setData(data: Movie) {
        binding?.apply {
            Glide.with(this@DetailActivity)
                .load(this@DetailActivity.getString(R.string.url_img) + data.poster_path)
                .placeholder(R.drawable.ic_launcher_background).into(ivAvatar)
            if (data.original_name != null) tvTitle.text = data.original_name
            if (data.original_title != null) tvTitle.text = data.original_title
            if (data.first_air_date != null) tvDate.text = data.first_air_date
            if (data.release_date != null) tvDate.text = data.release_date
            tvOverview.text = data.overview
            setProgressTo(data.vote_average)
        }
    }

    private fun setProgressTo(progress: Double) {
        binding?.apply {
            include.progressTv.text = "$progress%"
            include.circularDeterminativePb.progress = (progress * 10).toInt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        binding1 = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): com.example.moviecatalogue.favorite.viewmodel.MovieViewModel {
        val factory =
            com.example.moviecatalogue.favorite.viewmodel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            activity,
            factory
        ).get(com.example.moviecatalogue.favorite.viewmodel.MovieViewModel::class.java)
    }

    //change favorite icon
    private fun setStatus(isActive: Boolean) {
        val myFabSrc = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_favorite_24,
            null
        )
        val illBeWhite = myFabSrc?.constantState?.newDrawable()
        if (isActive) {
            illBeWhite?.mutate()?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.RED,
                    BlendModeCompat.SRC_ATOP
                )
            binding?.favButton?.setImageDrawable(illBeWhite)
        } else {
            illBeWhite?.mutate()?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.WHITE,
                    BlendModeCompat.SRC_ATOP
                )
            binding?.favButton?.setImageDrawable(illBeWhite)
        }
    }
}