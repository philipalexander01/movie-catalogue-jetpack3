package com.example.moviecatalogue.favorite

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.TabViewAdapter
import com.example.moviecatalogue.databinding.ActivityFavoriteBinding
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_movie,
            R.string.tab_tv
        )
    }
    private var binding: ActivityFavoriteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = this.getString(R.string.fav)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val tabViewAdapter = TabViewAdapter(this,getString(R.string.fav))
        binding?.apply {
            viewPager.adapter = tabViewAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}