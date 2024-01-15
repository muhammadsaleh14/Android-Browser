package com.example.browserapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.browserapp.activities.WebViewActivity
import com.example.browserapp.databinding.HistoryItemLayoutBinding
import com.example.browserapp.models.UserHistory

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
        holder.binding.historyName.setText(history.name)
        holder.binding.historyUrl.setText(history.url)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url",history.url)
            intent.putExtra("name",history.name)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}