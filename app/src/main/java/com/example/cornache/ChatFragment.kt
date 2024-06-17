package com.example.cornache

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.RoomAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.DataItem
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.FragmentChatBinding
import com.example.cornache.viewmodel.RoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class ChatFragment : Fragment() {
    private var _binding:FragmentChatBinding?= null
    private val binding get() = _binding
    private lateinit var viewModel:RoomViewModel
    private lateinit var adapter:RoomAdapter
    private lateinit var roomList:ArrayList<DataItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginPreference = LoginPreference.getInstance(requireContext().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), loginPreference)
        viewModel = ViewModelProvider(this, factory)[RoomViewModel::class.java]
        roomList = arrayListOf()
        adapter = RoomAdapter(roomList)
        binding?.buttonAdd?.setOnClickListener { replaceFragment(AddRoomFragment()) }
        getRoomList()
        val searchView = binding?.searchBar
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList(newText.toString())
                return true
            }

        })
        adapter.setOnItemClickCallback(object : RoomAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItem) {
                val bundle = Bundle().apply {
                    putString("room_id", data.detailRoom?.roomId)
                }
                val fragment = DetailRoomFragment()
                fragment.arguments = bundle
                replaceFragment(fragment)
            }

        })
        binding?.appbarItem?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.myDiscussion -> {
                    replaceFragment(MyRoomListFragment())
                    true
                }
                else -> false
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentChatBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun getRoomList(){
        binding?.rvChat?.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvChat?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(),layoutManager.orientation)
        binding?.rvChat?.addItemDecoration(itemDecoration)
        viewModel.getRoomList().observe(requireActivity()){result ->
            if (result!= null){
                when(result){
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        result.data?.map {
                            it?.let { it1 -> roomList.add(it1) }
                        }

                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading:Boolean){
        binding?.progressBar3?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.chatFragment,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    fun searchList(text:String){
        val searchList = ArrayList<DataItem>()
        for (room in roomList){
            if (room.detailRoom?.name?.lowercase()?.contains(text) == true){
                searchList.add(room)
            }
        }
        adapter.searchDataList(searchList)
    }

    companion object {

    }
}