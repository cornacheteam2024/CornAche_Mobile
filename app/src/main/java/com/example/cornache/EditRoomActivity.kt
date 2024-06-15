package com.example.cornache

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityEditRoomBinding
import com.example.cornache.viewmodel.AddRoomViewModel
import com.example.cornache.viewmodel.EditRoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class EditRoomActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditRoomBinding
    private lateinit var viewModel: EditRoomViewModel
    private lateinit var roomId:String
    private var currentImageUri:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory : ViewModelFactory = ViewModelFactory.getInstance(this, loginPreference)
        viewModel = ViewModelProvider(this,factory)[EditRoomViewModel::class.java]
        val bundle = intent.getBundleExtra(ROOM_DATA)
        roomId = bundle?.getString("roomId").toString()
        val name = bundle?.getString("name").toString()
        val description = bundle?.getString("description").toString()
        val image = bundle?.getString("image").toString()
        if (bundle!=null){
            binding.apply {
                GlideApp.with(this@EditRoomActivity)
                    .load(image)
                    .into(imagePlaceholder)
                judulText.setText(name)
                DeskripsiText.setText(description)
            }
        }
        binding.imagePlaceholder.setOnClickListener { startGallery() }
        binding.postRoom.setOnClickListener { updateRoom() }
        binding.deleteRoom.setOnClickListener { deleteRoom() }
    }

    private fun deleteRoom(){
        viewModel.deleteRoom(roomId).observe(this){result ->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        showToast(result.data.message.toString())
                        Intent(this@EditRoomActivity,RoomActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }
    private fun updateRoom(){
        currentImageUri?.let {
            val updatedName = binding.judulText.text.toString()
            val updatedDesc = binding.DeskripsiText.text.toString()
            val updatedImage = uriToFile(it,this)
            viewModel.updateRoom(roomId,updatedName,updatedImage,updatedDesc).observe(this){result ->
                if (result!=null){
                    when(result){
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Success -> {
                            showLoading(false)
                            Intent(this@EditRoomActivity,RoomActivity::class.java).also {
                                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }
    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun showImage() {
        currentImageUri?.let {
            binding.imagePlaceholder.setImageURI(it)
        }
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Toast.makeText(this, "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object{
        const val ROOM_DATA = "room_data"
    }
}