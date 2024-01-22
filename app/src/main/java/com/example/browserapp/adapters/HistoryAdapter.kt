package com.example.browserapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.browserapp.activities.WebViewActivity
import com.example.browserapp.databinding.HistoryItemLayoutBinding
import com.example.browserapp.models.UserHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter (
    private val context: Context,
    private var dataList:MutableList<UserHistory>
) : Adapter<HistoryAdapter.itemViewHolder>() {

    inner class itemViewHolder(val binding:HistoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){}

    fun updateList(list:MutableList<UserHistory>){
        dataList=list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  HistoryItemLayoutBinding.inflate(inflater,parent,false)
        return itemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val history = dataList[position]
        holder.binding.historyName.text = history.name
        holder.binding.historyUrl.text = history.url
        val timeStamp = history.timestamp
        val date = Date(timeStamp)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())  // Adjust format as needed
        val readableTime = dateFormat.format(date)
        holder.binding.timeStamp.text = readableTime
//        Log.d("timestampa","${timeStamp}")


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url",history.url)
            intent.putExtra("name",history.name)
            val activity = holder.itemView.context as Activity
            holder.itemView.context.startActivity(intent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}