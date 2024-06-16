package com.example.cornache

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.CommentsAdapter
import com.example.cornache.adapter.HistoryAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityDetailRoomBinding
import com.example.cornache.viewmodel.DetailRoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DetailRoomActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailRoomBinding
    private lateinit var viewModel: DetailRoomViewModel
    private lateinit var roomId: String
    private val loginPreference: LoginPreference = LoginPreference.getInstance(dataStore)
    private lateinit var adapter: CommentsAdapter
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
        adapter = CommentsAdapter()
        binding.rvComment.adapter = adapter
        binding.rvComment.layoutManager = LinearLayoutManager(this)
        fetchData()
        binding.replyRoom.setOnClickListener {
            val comment = binding.commentEditText.text.toString()
            if (comment.isNotBlank()) {
                postComment(roomId,comment)
                binding.commentEditText.text?.clear()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }else{
                Toast.makeText(this, "Komen tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        fetchComments(roomId)
//        setupNavigation()
    }

    private fun fetchData(){
        roomId = intent.getStringExtra(ROOM_ID).toString()
        viewModel.getDetailRoom(roomId).observe(this){result ->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                        showData(false)
                    }
                    is ResultState.Success -> {
                        showLoading(false)
                        showData(true)
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
                        showLoading(false)
                        showData(true)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun fetchComments(roomId:String){
        viewModel.comments(roomId).observe(this,{
            adapter.submitData(lifecycle,it)
        })
    }

    private fun postComment(roomId: String, content:String){
        viewModel.postComment(roomId,content).observe(this){result->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        showLoading(false)
                        fetchComments(roomId)
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showImage(isPresent:Boolean){
        if (isPresent) binding.ivRoom.visibility=View.VISIBLE else binding.ivRoom.visibility=View.GONE
    }

    private fun showLoading(isLoading:Boolean){
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showData(isLoading:Boolean){
        binding.data.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun setupNavigation() {
//        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
//        bottomNavigationView.selectedItemId = R.id.navigation_chat
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_history -> {
//                    startActivity(Intent(this, HistoryActivity::class.java))
//                    true
//                }
//                R.id.navigation_detect_disease -> {
//                    startActivity(Intent(this, AnalyzeActivity::class.java))
//                    true
//                }
//                R.id.navigation_edit_profile -> {
//                    startActivity(Intent(this, EditProfileActivity::class.java))
//                    true
//                }
//                R.id.navigation_logout -> {
//                    logout()
//                    true
//                }
//                R.id.navigation_chat -> {
//                    startActivity(Intent(this, RoomActivity::class.java))
//                    true
//                }
//                else -> false
//            }
//        }
//    }

    private fun logout() {
        lifecycleScope.launch {
            loginPreference.logout()
            startActivity(Intent(this@DetailRoomActivity, LoginActivity::class.java))
            finish()
        }
    }

    companion object{
        const val ROOM_ID = "room_id"
    }
}