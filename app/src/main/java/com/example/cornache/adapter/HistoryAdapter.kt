package com.example.cornache.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cornache.GlideApp
import com.example.cornache.data.api.response.History
import com.example.cornache.data.api.response.HistoryItem
import com.example.cornache.data.api.response.Prediction
import com.example.cornache.databinding.ItemHistoryBinding

class HistoryAdapter : PagingDataAdapter<HistoryItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data =getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }
    class MyViewHolder(val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryItem){
            binding.apply {
                GlideApp.with(itemView).load(data.prediction?.image).into(ivResult)
                tvResult.text = data.prediction?.name
                tvTime.text=data.prediction?.createdAt
            }


        }
    }
    interface OnItemClickCallback{
        fun onItemClicked(data:HistoryItem)
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>(){
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem== newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryItem,
                newItem: HistoryItem
            ): Boolean {
                return oldItem.userId == newItem.userId
            }

        }
    }
}
