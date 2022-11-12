package com.example.photogallery

import android.util.Log
import com.example.photogallery.api.FlickrApi
import com.example.photogallery.api.FlickrResponse
import com.example.photogallery.api.PhotoResponse
import com.example.photogallery.model.GalleryItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrFetch"

class FlickrFetchr {

    private val flickrApi: FlickrApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    suspend fun fetchPhotos(): List<GalleryItem> {
        return try {
            val flickrRequest: Response<FlickrResponse> = flickrApi.fetchPhotos()
            val flickrResponse: FlickrResponse? = flickrRequest.body()
            val photoResponse: PhotoResponse? = flickrResponse?.photos
            var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                ?: mutableListOf()
            galleryItems = galleryItems.filterNot {
                it.url.isBlank()
            }
            Log.d(TAG, "Response received")
            galleryItems
        } catch (e: Exception) {
            Log.d(TAG, "Error on fetch photo", e)
            mutableListOf()
        }
    }
}