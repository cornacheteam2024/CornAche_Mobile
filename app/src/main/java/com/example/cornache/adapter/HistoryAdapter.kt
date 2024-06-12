package com.example.cornache.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cornache.data.api.response.Prediction
import com.example.cornache.databinding.ItemHistoryBinding

class HistoryListAdapter : PagingDataAdapter<Prediction, HistoryListAdapter.MyViewHolder>(DIFF_CALLBACK){
    class MyViewHolder(private val binding:ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Prediction){
            binding.apply {
                Glide.with(itemView)
                    .load(data.image)
                    .into(ivResult)
                tvResult.text = data.name
                tvTime.text = data.createdAt
            }
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }
    companion object{
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Prediction>(){
        override fun areItemsTheSame(oldItem: Prediction, newItem: Prediction): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Prediction, newItem: Prediction): Boolean {
            return oldItem == newItem
        }

    }
    }
}