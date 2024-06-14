package com.example.cornache

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityDetailRoomBinding
import com.example.cornache.viewmodel.DetailRoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class DetailRoomActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailRoomBinding
    private lateinit var viewModel: DetailRoomViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val preference = LoginPreference.getInstance(dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, preference)
        viewModel = ViewModelProvider(this, factory)[DetailRoomViewModel::class.java]
        fetchData()
    }

    private fun fetchData(){
        val userId = intent.getStringExtra(ROOM_ID)
        viewModel.getDetailRoom(userId.toString()).observe(this){result ->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        val detailRoom = result.data.room?.detailRoom
                        binding.tvUsername.text = result.data.room?.username
                        if (detailRoom?.image.isNullOrEmpty()) {
                            showImage(false)
                            binding.apply {
                                titleDiscussion.text = detailRoom?.name
                                tvPreview.text = detailRoom?.description
                                tvCreateAt.text = detailRoom?.createdAt
                            }
                        }else{
                            showImage(true)
                            binding.apply {
                                GlideApp.with(this@DetailRoomActivity)
                                    .load(detailRoom?.image)
                                    .into(ivRoom)
                                titleDiscussion.text = detailRoom?.name
                                tvPreview.text = detailRoom?.description
                                tvCreateAt.text = detailRoom?.createdAt
                            }
                        }
                    }
                    is ResultState.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showImage(isPresent:Boolean){
        if (isPresent) binding.ivRoom.visibility=View.VISIBLE else binding.ivRoom.visibility=View.GONE
    }

    companion object{
        const val ROOM_ID = "room_id"
    }
}