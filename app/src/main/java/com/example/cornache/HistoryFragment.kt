package com.example.cornache

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.HistoryAdapter
import com.example.cornache.adapter.LoadingStateAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.FragmentHistoryBinding
import com.example.cornache.viewmodel.HistoryViewModel
import com.example.cornache.viewmodel.RoomViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding:FragmentHistoryBinding?=null
    private val binding get() = _binding
    private lateinit var viewModel:HistoryViewModel
    private lateinit var adapter:HistoryAdapter
    private lateinit var preference: LoginPreference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = LoginPreference.getInstance(requireContext().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), preference)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        binding?.rvHistory?.layoutManager = LinearLayoutManager(requireContext())
        showLoading(true)
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding?.root
    }

    private fun getData() {
        adapter = HistoryAdapter()
        binding?.rvHistory?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.history.observe(requireActivity(),{
            adapter.submitData(lifecycle,it)
        })
        showLoading(false)
    }
    private fun showLoading(isLoading:Boolean){
        binding?.progressBar3?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {

    }
}