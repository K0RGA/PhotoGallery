package com.example.photogallery

import androidx.constraintlayout.widget.ConstraintSet.Transform
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.photogallery.model.GalleryItem

class PhotoGalleryViewModel: ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    init {
        mutableSearchTerm.value = "planet"

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            liveData { emit(flickrFetchr.searchPhotos(searchTerm))  }
        }
    }

    fun fetchPhotos(query: String = ""){
        mutableSearchTerm.value = query
    }
}