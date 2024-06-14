package com.example.cornache.data

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.example.cornache.data.api.response.HistoryItem
import com.example.cornache.data.api.retrofit.GETApiService

class HistoryPagingSource(private val apiService: GETApiService, private val userId:String) : PagingSource<Int, HistoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, HistoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getHistory(userId,position)
            Page(
                data = responseData.history,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.history.isNullOrEmpty()) null else position + 1
            )
        }catch (e:Exception){
            return LoadResult.Error(e)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}