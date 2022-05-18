package com.harry.moviesapp.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.harry.moviesapp.base.BaseFragment
import com.harry.moviesapp.databinding.FragmentHomeBinding
import com.harry.moviesapp.ui.adapter.MovieAdapter
import com.harry.moviesapp.viewModel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeFragmentViewModel by viewModels()

    lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridLayoutManager = object : GridLayoutManager(
            this.context,
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> PORTRAIT_COUNT
                Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE_COUNT
                else -> PORTRAIT_COUNT
            }
        ) {
            override fun canScrollHorizontally(): Boolean {
                return !binding.rvPhotos.layoutManager!!.isSmoothScrolling
            }
        }

        adapter = MovieAdapter()
        bind(binding, gridLayoutManager)
        setUpSearch()
    }

    private fun bind(binding: FragmentHomeBinding, gridLayoutManager: GridLayoutManager) {
        binding.apply {
            rvPhotos.adapter = adapter
            rvPhotos.layoutManager = gridLayoutManager
        }

        viewModel.run {
            movieData.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            errorMessage.observe(viewLifecycleOwner) {
                it?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT) }
                binding.txtNoConnection.visibility = View.VISIBLE
                binding.txtNoConnection.text = it
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.pbLoadingItems.visibility = View.VISIBLE
                } else {
                    binding.pbLoadingItems.visibility = View.GONE
                }
            }
        }
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private fun setUpSearch() {
        binding.simpleSearchView.doOnTextChanged { text, start, before, count ->
            lifecycleScope.launch { viewModel.filterMovies(text.toString()) }
        }
    }

    companion object {
        private const val PORTRAIT_COUNT = 3
        private const val LANDSCAPE_COUNT = 6
    }

}