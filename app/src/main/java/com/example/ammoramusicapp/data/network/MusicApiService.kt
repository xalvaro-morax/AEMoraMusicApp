package com.example.ammoramusicapp.data.network

import com.example.ammoramusicapp.data.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApiService {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumDetail(@Path("id") id: Int): Album
}
