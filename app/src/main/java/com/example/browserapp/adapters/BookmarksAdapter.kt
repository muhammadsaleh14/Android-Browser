package com.example.browserapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.browserapp.databinding.BookmarkItemLayoutBinding
import com.example.browserapp.models.UserBookmark

class BookmarksAdapter (
    private val context: Context,
    private var dataList:MutableList<UserBookmark>
) : Adapter<BookmarksAdapter.itemViewHolder>() {


    inner class itemViewHolder(val binding:BookmarkItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){}

    fun updateList(list:MutableList<UserBookmark>){
        dataList=list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  BookmarkItemLayoutBinding.inflate(inflater,parent,false)
        return itemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val bookmark = dataList[position]
        holder.binding.bookmarkName.setText(bookmark.name)
        holder.binding.bookmarkUrl.setText(bookmark.url)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}