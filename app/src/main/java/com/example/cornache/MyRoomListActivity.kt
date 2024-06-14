package com.example.cornache

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import com.example.cornache.databinding.ActivityMyRoomListBinding
import com.example.cornache.viewmodel.MyRoomListViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyRoomListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyRoomListBinding
    private lateinit var viewModel: MyRoomListViewModel
    private lateinit var adapter:RoomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyRoomListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val preference = LoginPreference.getInstance(dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this,preference)
        viewModel = ViewModelProvider(this, factory)[MyRoomListViewModel::class.java]
        adapter = RoomAdapter()
        fetchData()
        adapter.setOnItemClickCallback(object : RoomAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItem) {
                Intent(this@MyRoomListActivity,EditRoomActivity::class.java).also {
                    val bundle = Bundle().apply {
                        putString("roomId", data.detailRoom?.roomId)
                        putString("name",data.detailRoom?.name)
                        putString("description", data.detailRoom?.description)
                        putString("image",data.detailRoom?.image)
                    }
                    it.putExtra(EditRoomActivity.ROOM_DATA,bundle)
                    startActivity(it)
                }
            }

        })
    }
    private fun fetchData(){
        binding.rvDiskusiSaya.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvDiskusiSaya.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvDiskusiSaya.addItemDecoration(itemDecoration)
        val preferences = LoginPreference.getInstance(dataStore)
        val userId = runBlocking { preferences.getSession().first().userId }
        viewModel.getRoomList().observe(this){result ->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        showLoading(false)
                        val listRoom = result.data
                        if (listRoom != null) {
                            for (room in listRoom){
                                if (room?.userId == userId){
                                    val roomSaya = mutableListOf(room)
                                    adapter.submitList(roomSaya)
                                }
                            }
                        }

                    }
                    is ResultState.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
            }
        }
    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}