package com.example.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    @GET
    suspend fun fetchUrlBytes(@Url url: String): Response<ResponseBody>

    @GET("services/rest/?method=flickr.interestingness.getList")
    suspend fun fetchPhotos(): Response<FlickrResponse>

    @GET("services/rest/?method=flickr.photos.search")
    suspend fun searchPhotos(@Query("text") query: String): Response<FlickrResponse>

    @GET("services/rest/?method=flickr.interestingness.getList")
    fun fetchPhotosCall(): Call<FlickrResponse>

    @GET("services/rest/?method=flickr.photos.search")
    fun searchPhotosCall(@Query("text") query: String): Call<FlickrResponse>
}