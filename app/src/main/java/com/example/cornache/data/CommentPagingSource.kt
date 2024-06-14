package com.example.cornache.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cornache.data.api.response.ChatsItem
import com.example.cornache.data.api.retrofit.ApiService
import com.example.cornache.data.api.retrofit.GETApiService

class CommentPagingSource(private val apiService: GETApiService, private val roomId:String) :
    PagingSource<Int,ChatsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ChatsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllComment(roomId,position)
            LoadResult.Page(
                data = responseData.chats,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.chats.isNullOrEmpty()) null else position +1
            )
        }catch (e:Exception){
            return LoadResult.Error(e)
        }
    }
    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}