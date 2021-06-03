package com.example.moviecatalogue.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviecatalogue.fragment.MovieFragment

class TabViewAdapter(activity: AppCompatActivity,val page:String) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return MovieFragment.newInstance(position + 1,page)
    }


    override fun getItemCount(): Int {
        return 2
    }

}