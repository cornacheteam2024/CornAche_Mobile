package com.example.cornache.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cornache.GlideApp
import com.example.cornache.data.api.response.DataItem
import com.example.cornache.data.api.response.User
import com.example.cornache.databinding.ItemChatBinding
import java.text.SimpleDateFormat
import java.util.Locale

class RoomAdapter(private var roomList: ArrayList<DataItem>):RecyclerView.Adapter<RoomAdapter.MyViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    var destinationListFiltered : ArrayList<DataItem> = ArrayList()

    fun setOnItemClickCallback(onItemClickCallback: RoomAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    inner class MyViewHolder(val binding: ItemChatBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    onItemClickCallback?.onItemClicked(roomList[position])
                }
            }
        }
        fun bind(data: DataItem) {
            if (data.detailRoom?.updateAt == null) {
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
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    fun searchDataList(searchList:ArrayList<DataItem>){
        roomList = searchList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = roomList[position]
        if (data != null){
            holder.bind(data)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data:DataItem)
    }

}