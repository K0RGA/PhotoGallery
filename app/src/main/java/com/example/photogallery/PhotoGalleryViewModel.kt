package com.example.photogallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.photogallery.api.FlickrFetchr
import com.example.photogallery.model.GalleryItem
import com.example.photogallery.utils.QueryPreferences

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""

    init {
        mutableSearchTerm.value = ""

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {
                liveData { emit(flickrFetchr.fetchPhotos()) }
            } else {
                liveData { emit(flickrFetchr.searchPhotos(searchTerm)) }
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoryQuery(app, query)
        mutableSearchTerm.value = query
    }
}