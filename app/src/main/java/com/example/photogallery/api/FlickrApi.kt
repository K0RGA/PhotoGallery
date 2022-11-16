package com.example.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=7eceabcb232cac6c22871f2427fb6bee" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    suspend fun fetchPhotos(): Response<FlickrResponse>

    @GET
    suspend fun fetchUrlBytes(@Url url: String): Response<ResponseBody>
}