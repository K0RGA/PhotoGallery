package com.example.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.photogallery.model.GalleryItem

class PhotoGalleryViewModel: ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>> = liveData { emit(FlickrFetchr().fetchPhotos()) }
}