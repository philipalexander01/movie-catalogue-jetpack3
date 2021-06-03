package com.example.moviecatalogue.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalogue.R
import com.example.moviecatalogue.databinding.ItemListBinding
import com.example.moviecatalogue.model.Movie

class MoviePagedListAdapter :
    PagedListAdapter<Movie, MoviePagedListAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem == newItem
                }
            }
    }

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie){
            with(binding){
            ivPoster.loadImage(movie.poster_path)
                    if (movie.original_name != null) tvTitle.text = movie.original_name
                    if (movie.original_title != null) tvTitle.text = movie.original_title
                    tvOverview.text = movie.overview


            }
        }
       private fun ImageView.loadImage(url: String) {
            Glide.with(this.context).load(context.getString(R.string.url_img) + url)
                .placeholder(R.drawable.ic_launcher_background).into(this)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) as Movie)
        holder.binding.materialCardView.setOnClickListener {
            onItemClickCallback.onItemClick(getItem(position) as Movie)
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Movie)
    }


}