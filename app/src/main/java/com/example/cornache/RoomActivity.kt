package com.example.cornache

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.RoomAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.DataItem
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityChatBinding
import com.example.cornache.viewmodel.RoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: RoomViewModel
    private lateinit var adapter: RoomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, loginPreference)
        viewModel = ViewModelProvider(this, factory)[RoomViewModel::class.java]
        adapter = RoomAdapter()
        binding.buttonAdd.setOnClickListener { startActivity(Intent(this@RoomActivity,AddRoomActivity::class.java)) }
        getRoomList()
        adapter.setOnItemClickCallback(object : RoomAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItem) {
                Intent(this@RoomActivity, DetailRoomActivity::class.java).also {
                    it.putExtra(DetailRoomActivity.ROOM_ID, data.detailRoom?.roomId)
                    startActivity(it)
                }
            }

        })
        binding.appbarItem.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.myDiscussion -> {
                    startActivity(Intent(this@RoomActivity, MyRoomListActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    private fun getRoomList(){
        binding.rvChat.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvChat.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvChat.addItemDecoration(itemDecoration)

        viewModel.getRoomList().observe(this){result ->
            if (result!= null){
                when(result){
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        val roomList = result.data
                        adapter.submitList(roomList)
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading:Boolean){
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}