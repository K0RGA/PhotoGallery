package com.example.photogallery.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=7eceabcb232cac6c22871f2427fb6bee" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    suspend fun fetchPhotos(): Response<FlickrResponse>
}