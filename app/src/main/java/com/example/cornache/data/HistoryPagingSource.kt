package com.example.cornache.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cornache.data.api.retrofit.ApiService
import com.example.cornache.data.api.response.Prediction
import com.example.cornache.data.api.retrofit.HistoryApiService
import okio.IOException
import retrofit2.HttpException

class HistoryPagingSource(private val apiService: HistoryApiService, private val userId:String) : PagingSource<Int, Prediction>() {
    override fun getRefreshKey(state: PagingState<Int, Prediction>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage =state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Prediction> {
        return try {
            val position =params.key ?:INITIAL_PAGE_INDEX
            val userId = ""
            val responseData = apiService.getHistory(userId,position)

            LoadResult.Page(
                data = responseData,
                nextKey = if (position == 1) null else position - 1,
                prevKey = if (responseData == null) null else position + 1
            )
        }catch (e:IOException){
            LoadResult.Error(e)
        }catch (e:HttpException){
            LoadResult.Error(e)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}