package com.example.cornache

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.CommentsAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityDetailRoomBinding
import com.example.cornache.databinding.FragmentDetailRoomBinding
import com.example.cornache.viewmodel.DetailRoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory


class DetailRoomFragment : Fragment() {
    private var _binding: FragmentDetailRoomBinding?=null
    private val binding get()=_binding
    private lateinit var viewModel: DetailRoomViewModel
    private lateinit var roomId: String
    private lateinit var adapter: CommentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        val roomId = bundle?.getString("room_id").toString()
        val preference = LoginPreference.getInstance(requireContext().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), preference)
        viewModel = ViewModelProvider(this, factory)[DetailRoomViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
        adapter = CommentsAdapter()
        binding?.rvComment?.adapter = adapter
        binding?.rvComment?.layoutManager = LinearLayoutManager(requireContext())
        fetchData()
        binding?.replyRoom?.setOnClickListener {
            val comment = binding?.commentEditText?.text.toString()
            if (comment.isNotBlank()) {
                postComment(roomId,comment)
                binding?.commentEditText?.text?.clear()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }else{
                Toast.makeText(requireContext(), "Komen tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        fetchComments(roomId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailRoomBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun fetchData(){
        val bundle = this.arguments
        val roomId = bundle?.getString("room_id").toString()
        viewModel.getDetailRoom(roomId).observe(requireActivity()){result ->
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
                        binding?.tvUsername?.text = result.data.room?.username
                        if (detailRoom?.image.isNullOrEmpty()) {
                            showImage(false)
                            binding?.apply {
                                titleDiscussion.text = detailRoom?.name
                                tvPreview.text = detailRoom?.description
                                tvCreateAt.text = detailRoom?.createdAt
                            }
                        }else{
                            showImage(true)
                            binding?.apply {
                                GlideApp.with(requireContext())
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
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun fetchComments(roomId:String){
        viewModel.comments(roomId).observe(requireActivity(),{
            adapter.submitData(lifecycle,it)
        })
    }

    private fun postComment(roomId: String, content:String){
        viewModel.postComment(roomId,content).observe(requireActivity()){result->
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
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showImage(isPresent:Boolean){
        if (isPresent) binding?.ivRoom?.visibility=View.VISIBLE else binding?.ivRoom?.visibility=View.GONE
    }

    private fun showLoading(isLoading:Boolean){
        binding?.progressBar3?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showData(isLoading:Boolean){
        binding?.data?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {

    }
}