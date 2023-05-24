package com.example.yourstorymyapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.yourstorymyapp.data.api.ApiService
import com.example.yourstorymyapp.data.model.ListStoryItem

class Repository(
    private val apiService: ApiService
) {

    fun getDataForPaging(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PagingSource(apiService, token)
            }
        ).liveData
    }
}