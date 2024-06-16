package com.example.cornache

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.MyRoomListAdapter
import com.example.cornache.adapter.RoomAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.DataItem
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityMyRoomListBinding
import com.example.cornache.databinding.FragmentMyRoomListBinding
import com.example.cornache.viewmodel.MyRoomListViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyRoomListFragment : Fragment() {
    private var _binding : FragmentMyRoomListBinding?=null
    private val binding get()=_binding
    private lateinit var viewModel: MyRoomListViewModel
    private lateinit var adapter: MyRoomListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = LoginPreference.getInstance(requireContext().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(),preference)
        viewModel = ViewModelProvider(this, factory)[MyRoomListViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
        adapter = MyRoomListAdapter()
        fetchData()
        adapter.setOnItemClickCallback(object : MyRoomListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataItem) {
                val bundle = Bundle().apply {
                    putString("roomId", data.detailRoom?.roomId)
                    putString("name",data.detailRoom?.name)
                    putString("description", data.detailRoom?.description)
                    putString("image",data.detailRoom?.image)
                }
                val fragment = EditRoomFragment()
                fragment.arguments = bundle
                replaceFragment(fragment)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyRoomListBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun fetchData(){
        binding?.rvDiskusiSaya?.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvDiskusiSaya?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(),layoutManager.orientation)
        binding?.rvDiskusiSaya?.addItemDecoration(itemDecoration)
        val preferences = LoginPreference.getInstance(requireContext().dataStore)
        val userId = runBlocking { preferences.getSession().first().userId }
        viewModel.getRoomList().observe(requireActivity()){result ->
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
                                    val myRoom = mutableListOf(room)
                                    adapter.submitList(myRoom)
                                }
                            }
                        }

                    }
                    is ResultState.Error -> {
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
        fragmentTransaction.replace(R.id.myRoomFragment,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {

    }
}