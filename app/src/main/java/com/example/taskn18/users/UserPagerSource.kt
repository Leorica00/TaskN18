package com.example.taskn18.users

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taskn18.users.api.UsersApi
import com.example.taskn18.users.model.User

class UserPagingSource(
    private val apiService: UsersApi,
    private val perPage: Int
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getUsers(nextPageNumber, perPage)

            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response.data.isEmpty()) null else nextPageNumber + 1
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}


