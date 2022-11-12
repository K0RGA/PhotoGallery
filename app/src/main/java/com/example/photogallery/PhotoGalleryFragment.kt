package com.example.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.model.GalleryItem

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment() {

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()
    private lateinit var photoRecyclerView: RecyclerView
    private var adapter = PhotoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)
        photoRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                adapter.submitList(ArrayList(galleryItems))
            })
    }

    private class PhotoHolder( itemTextView: TextView) : RecyclerView.ViewHolder(itemTextView) {

        val textView = itemTextView

        fun bindTitle(str: String) {
            textView.text = str
        }
    }

    private class PhotoAdapter() :
        ListAdapter<GalleryItem, PhotoHolder>(FlickrCallback()) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PhotoHolder {
            val textView = TextView(parent.context)
            return PhotoHolder(textView)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = currentList[position]
            holder.bindTitle(galleryItem.title)
        }
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}