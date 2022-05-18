package com.harry.moviesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harry.moviesapp.R
import com.harry.moviesapp.data.Data
import com.harry.moviesapp.databinding.ItemMovieListBinding

class MovieAdapter() : ListAdapter<Data, MovieAdapter.MovieViewHolder>(MovieDiffCallBack()) {


    inner class MovieViewHolder(private val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Data) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.poster)
                    .error(Glide.with(itemView.context).load(R.drawable.ic_poster_error))
                    .into(cvIvPhotoPoster)
                genre.text = data.genre
                movieTitle.text = data.title
                movieYear.text = data.year
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

class MovieDiffCallBack : DiffUtil.ItemCallback<Data>() {
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem.id == newItem.id
    }

}