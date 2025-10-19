package com.example.ammoramusicapp.data.network

import com.example.ammoramusicapp.data.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApiService {
    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumDetail(@Path("id") id: String): Album
}
