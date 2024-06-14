package com.example.cornache.adapter

import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cornache.GlideApp
import com.example.cornache.data.api.response.DataItem
import com.example.cornache.data.api.response.User
import com.example.cornache.databinding.ItemChatBinding
import java.text.SimpleDateFormat
import java.util.Locale

class RoomAdapter:ListAdapter<DataItem, RoomAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    inner class MyViewHolder(val binding : ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    onItemClickCallback?.onItemClicked(getItem(position))
                }
            }
        }
        fun bind(data:DataItem){
            if (data.detailRoom?.updateAt == null){
                binding.apply {
                    tvUsername.text = data.username
                    titleDiscussion.text = data.detailRoom?.name
                    tvPreview.text = data.detailRoom?.description
                    tvCreateAt.text = data.detailRoom?.createdAt
                }
            } else {
                binding.apply {
                    tvUsername.text = data.username
                    titleDiscussion.text = data.detailRoom.name
                    tvPreview.text = data.detailRoom.description
                    tvCreateAt.text = data.detailRoom.updateAt
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    interface OnItemClickCallback{
        fun onItemClicked(data:DataItem)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>(){
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}