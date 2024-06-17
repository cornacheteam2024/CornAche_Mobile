package com.example.cornache

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.FragmentAddRoomBinding
import com.example.cornache.viewmodel.AddRoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class AddRoomFragment : Fragment() {
    private var _binding : FragmentAddRoomBinding?=null
    private val binding get() = _binding
    private lateinit var loginPreference: LoginPreference
    private lateinit var viewModel : AddRoomViewModel
    private var currentImageUri: Uri?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginPreference = LoginPreference.getInstance(requireContext().dataStore)
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireContext(), loginPreference)
        viewModel = ViewModelProvider(this,factory)[AddRoomViewModel::class.java]
        binding?.apply {
            imagePlaceholder.setOnClickListener { startGallery() }
            postRoom.setOnClickListener { createRoom() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRoomBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun createRoom(){
        currentImageUri?.let {
            val imageFile = uriToFile(it,requireContext())
            val name = binding?.judulText?.text.toString()
            val description = binding?.DeskripsiText?.text.toString()
            viewModel.createRoom(name,imageFile,description).observe(requireActivity()){result ->
                if (result!=null){
                    when(result){
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Success -> {
                            showLoading(false)
                            Intent(requireContext(),HomeActivity::class.java).also {
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
            binding?.imagePlaceholder?.setImageURI(it)
        }
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
    private fun showLoading(isLoading:Boolean){
        binding?.progressBar3?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {

    }
}