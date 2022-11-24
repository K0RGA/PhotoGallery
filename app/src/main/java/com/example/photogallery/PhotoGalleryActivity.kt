package com.example.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView

class PhotoGalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        val isFragmentContainerEmpty = (savedInstanceState == null)
        if (isFragmentContainerEmpty){
            supportFragmentManager.commit {
                add(R.id.fragmentContainer,PhotoGalleryFragment.newInstance())
            }
        }
    }
}