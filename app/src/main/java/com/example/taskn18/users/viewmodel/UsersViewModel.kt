package com.example.taskn18.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taskn18.users.UserPagingSource
import com.example.taskn18.users.api.RetrofitClient
import com.example.taskn18.users.api.UsersApi
import com.example.taskn18.users.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    companion object {
        private const val PAGE_SIZE = 30
    }

    private val apiService: UsersApi = RetrofitClient.usersService()
    private var _pagingDataFlow = emptyFlow<PagingData<User>>()
    val pagingDataFlow get() = _pagingDataFlow

    init {
        _pagingDataFlow = fetchUsersData()
    }

    private fun fetchUsersData(): Flow<PagingData<User>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)) { createPagingSource() }
            .flow
            .cachedIn(viewModelScope)
    }

    private fun createPagingSource(): UserPagingSource {
        return UserPagingSource(apiService, PAGE_SIZE)
    }

    fun refreshData() {
        viewModelScope.launch {
            createPagingSource().invalidate()
            _pagingDataFlow = fetchUsersData()
        }
    }
}