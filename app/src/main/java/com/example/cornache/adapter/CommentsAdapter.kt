package com.example.cornache.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cornache.GlideApp
import com.example.cornache.data.api.response.ChatsItem
import com.example.cornache.data.api.response.HistoryItem
import com.example.cornache.databinding.ItemCommentBinding
import com.example.cornache.databinding.ItemHistoryBinding

class CommentsAdapter : PagingDataAdapter<ChatsItem, CommentsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data =getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }
    class MyViewHolder(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatsItem){
            binding.apply {
                GlideApp.with(itemView).load(data.profile?.avatar).into(ivProfile)
                tvUsername.text = data.profile?.username
                tvCreateAt.text=data.timestamp
                tvComment.text = data.content
            }


        }
    }
    interface OnItemClickCallback{
        fun onItemClicked(data: HistoryItem)
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatsItem>(){
            override fun areItemsTheSame(oldItem: ChatsItem, newItem: ChatsItem): Boolean {
                return oldItem== newItem
            }

            override fun areContentsTheSame(
                oldItem: ChatsItem,
                newItem: ChatsItem
            ): Boolean {
                return oldItem.chatId == newItem.chatId
            }

        }
    }
}