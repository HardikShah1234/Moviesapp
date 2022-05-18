package com.harry.moviesapp.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harry.moviesapp.data.Data
import com.harry.moviesapp.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: MoviesRepository,
    application: Application
) : AndroidViewModel(application) {

    var movieData: MutableLiveData<List<Data>> = MutableLiveData()
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>(null)

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            if (checkInternetConnection()) {
                isLoading.postValue(true)
                kotlin.runCatching {
                    repository.getMoviesData().sortedBy { it.title }
                }.onSuccess {
                    movieData.postValue(it)
                    isLoading.postValue(false)
                }.onFailure {
                    errorMessage.postValue(it.message)
                    isLoading.postValue(false)
                }
                isLoading.postValue(false)
            } else {
                errorMessage.postValue("Internet not available")
            }
        }
    }

    suspend fun filterMovies(searchTerm: String) {
        movieData.value = repository.getMoviesData().filter { mvData ->
            mvData.genre.contains(searchTerm, ignoreCase = true) ||
                    mvData.title.contains(searchTerm, ignoreCase = true)
        }
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
