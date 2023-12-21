package com.example.taskn18.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taskn18.users.UserPagingSource
import com.example.taskn18.users.api.RetrofitClient
import com.example.taskn18.users.event.GetUsersDataEvent
import com.example.taskn18.users.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    companion object {
        private const val PAGE_SIZE = 30
    }

    private var _pagingDataFlow = emptyFlow<PagingData<User>>()
    val pagingDataFlow get() = _pagingDataFlow

    private fun fetchUsersData(): Flow<PagingData<User>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)) { createPagingSource() }
            .flow
            .cachedIn(viewModelScope)
    }

    private fun createPagingSource(): UserPagingSource {
        return UserPagingSource(RetrofitClient.usersService(), PAGE_SIZE)
    }

    private fun refreshData() {
        viewModelScope.launch {
            createPagingSource().invalidate()
            _pagingDataFlow = fetchUsersData()
        }
    }

    fun handleEvent(event: GetUsersDataEvent) {
        when (event) {
            is GetUsersDataEvent.InitialData -> viewModelScope.launch {
                _pagingDataFlow = fetchUsersData()
            }

            is GetUsersDataEvent.RefreshData -> viewModelScope.launch { refreshData() }
        }
    }
}