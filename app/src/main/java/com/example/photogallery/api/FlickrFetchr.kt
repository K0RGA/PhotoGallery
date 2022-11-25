package com.example.photogallery.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.photogallery.api.FlickrApi
import com.example.photogallery.api.FlickrResponse
import com.example.photogallery.api.PhotoInterceptor
import com.example.photogallery.api.PhotoResponse
import com.example.photogallery.model.GalleryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrFetch"

class FlickrFetchr {

    private val flickrApi: FlickrApi

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    suspend fun fetchPhotos(): List<GalleryItem> {
        return fetchPhotoMetaData(flickrApi.fetchPhotos())
    }

    suspend fun searchPhotos(str: String): List<GalleryItem>{
        return fetchPhotoMetaData(flickrApi.searchPhotos(str))
    }

    fun fetchPhotoMetaData(flickrRequest: Response<FlickrResponse>): List<GalleryItem> {
        return try {
            val flickrResponse: FlickrResponse? = flickrRequest.body()
            val photoResponse: PhotoResponse? = flickrResponse?.photos
            var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                ?: mutableListOf()
            galleryItems = galleryItems.filterNot {
                it.url.isBlank()
            }
            galleryItems
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    suspend fun fetchPhoto(url: String): Deferred<Bitmap?>{
        return CoroutineScope(Dispatchers.IO).async {
            val response = flickrApi.fetchUrlBytes(url)
            val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
            bitmap
        }
    }
}