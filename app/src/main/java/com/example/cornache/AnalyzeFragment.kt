package com.example.cornache

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.FragmentAnalyzeBinding
import com.example.cornache.viewmodel.AnalyzeViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding?=null
    private val binding get() = _binding
    private var currentImageUri: Uri?=null
    private lateinit var viewModel: AnalyzeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = LoginPreference.getInstance(requireContext().dataStore)
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireContext(), preference)
        viewModel = ViewModelProvider(this,factory)[AnalyzeViewModel::class.java]
        binding?.apply {
            btnAnalyze.setOnClickListener { uploadImage() }
            btnCamera.setOnClickListener { startCameraX() }
            btnGallery.setOnClickListener { startGallery() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalyzeBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri:Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Toast.makeText(requireContext(), "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            binding?.imagePlaceholder?.setImageURI(it)
        }
    }
    private fun startCameraX(){
        val intent = Intent(requireContext(),CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == CameraActivity.CAMERAX_RESULT){
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }
    private fun uploadImage(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri,requireContext())
            Log.d("ImageFile", "showImage:${imageFile.path} ")
            val preference = LoginPreference.getInstance(requireContext().dataStore)
            val userId = runBlocking { preference.getSession().first().userId }
            viewModel.analyzeImage(imageFile,userId).observe(requireActivity()) {result ->
                if (result != null){
                    when(result){
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showLoading(false)
                            val resultPrediction = result.data.history?.prediction?.name
                            val resultImage = result.data.history?.prediction?.image
                            val bundle = Bundle().apply {
                                putString("nama",resultPrediction)
                                putString("gambar",resultImage)
                            }
                            val confidenceScore:String = result.data.history?.prediction?.confidenceScore.toString()
                            if (confidenceScore.toInt() < 70){
                                Toast.makeText(requireContext(), "Gambar kurang jelas, pilih gambar lain!", Toast.LENGTH_SHORT).show()
                            }else{
                                Intent(requireContext(),ResultActivity::class.java).also {
                                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    it.putExtra(ResultActivity.DATA,bundle)
                                    startActivity(it)
                                }
                            }

                        }
                        is ResultState.Error ->{
                            showLoading(false)
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading:Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {

    }
}