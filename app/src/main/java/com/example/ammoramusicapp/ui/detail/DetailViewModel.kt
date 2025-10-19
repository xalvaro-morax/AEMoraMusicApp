package com.example.ammoramusicapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ammoramusicapp.data.model.Album
import com.example.ammoramusicapp.data.network.MusicApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://music.juanfrausto.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(MusicApiService::class.java)

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAlbum(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _album.value = api.getAlbumDetail(id)
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar el álbum"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
