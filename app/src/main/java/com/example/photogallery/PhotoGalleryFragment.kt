package com.example.photogallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.photogallery.model.GalleryItem
import com.example.photogallery.utils.FlickrCallback
import com.example.photogallery.utils.PollWorker
import com.example.photogallery.utils.VisibleFragment
import com.squareup.picasso.Picasso

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : VisibleFragment() {

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()
    private lateinit var photoRecyclerView: RecyclerView
    private var adapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        createWorkRequest()
    }

    private fun createWorkRequest() {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val workRequest = OneTimeWorkRequest
            .Builder(PollWorker::class.java)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance()
            .enqueue(workRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery,menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(queryString: String): Boolean {
                    photoGalleryViewModel.fetchPhotos(queryString)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })

            setOnSearchClickListener {
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_item_clear -> {
                photoGalleryViewModel.fetchPhotos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View     {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        createRecyclerView(view)
        return view
    }

    private fun createRecyclerView(view: View) {
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)
        photoRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createObserver()
    }

    private fun createObserver() {
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                adapter.submitList(ArrayList(galleryItems))
            })
    }

    private inner class PhotoHolder(private val itemImageView: ImageView) :
        RecyclerView.ViewHolder(itemImageView), View.OnClickListener {
        private lateinit var galleryItem: GalleryItem

        init {
            itemView.setOnClickListener(this)
        }

        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable

        fun bindGalleryItem(item: GalleryItem) {
            galleryItem = item
            Picasso.get().load(item.url).into(itemImageView)
        }

        override fun onClick(view: View) {
            val intent = PhotoPageActivity
                .newIntent(requireContext(), galleryItem.photoPageUri)
            startActivity(intent)
        }
    }

    private inner class PhotoAdapter() : ListAdapter<GalleryItem, PhotoHolder>(FlickrCallback()) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PhotoHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_gallery, parent, false) as ImageView
            return PhotoHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = currentList[position]
            holder.bindGalleryItem(galleryItem)
        }
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}