package com.example.cornache

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityEditProfileBinding
import com.example.cornache.databinding.FragmentEditProfileBinding
import com.example.cornache.viewmodel.EditProfileViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding?=null
    private val binding get()=_binding
    private lateinit var viewModel: EditProfileViewModel
    private var currentImageUri: Uri?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = LoginPreference.getInstance(requireContext().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), preference)
        viewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
        getDetailUser()
        binding?.apply {
            profileImage.setOnClickListener { startGallery() }
            btnSave.setOnClickListener { updateDetailUser() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun getDetailUser(){
        val preference = LoginPreference.getInstance(requireContext().dataStore)
        val userId = runBlocking { preference.getSession().first().userId }
        viewModel.getDetailUser(userId).observe(requireActivity()){result ->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        val detailData = result.data.user
                        if (detailData?.avatarImg.isNullOrEmpty()){
                            binding?.apply {
                                username.setText(detailData?.username)
                            }
                        }else{
                            binding?.apply {
                                GlideApp.with(requireContext())
                                    .load(detailData?.avatarImg)
                                    .into(profileImage)
                                username.setText(detailData?.username)
                            }
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
    private fun updateDetailUser(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri,requireContext())
            val updatedUsername = binding?.username?.text.toString()
            viewModel.updateDetailUser(updatedUsername,imageFile).observe(requireActivity()){result ->
                if (result!=null){
                    when(result){
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Success -> {
                            showLoading(false)
                            getDetailUser()
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Toast.makeText(requireContext(), "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            binding?.profileImage?.setImageURI(it)
        }
    }
    private fun showLoading(isLoading:Boolean){
        binding?.progressBar2?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {

    }
}