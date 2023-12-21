package com.example.taskn18.users

import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskn18.AppError
import com.example.taskn18.BaseFragment
import com.example.taskn18.databinding.FragmentUsersBinding
import com.example.taskn18.users.event.GetUsersDataEvent
import com.example.taskn18.users.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class UsersFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding::inflate) {

    private val userViewModel: UserViewModel by viewModels()
    private var usersAdapter = UsersRecyclerViewAdapter()

    override fun setUp() {
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = usersAdapter
        userViewModel.handleEvent(GetUsersDataEvent.InitialData)
    }

    override fun setUpObservers() {
        recyclerPagingDataObserver()
        loadStateObserver()
    }

    override fun setUpListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            userViewModel.handleEvent(GetUsersDataEvent.RefreshData)
            usersAdapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun recyclerPagingDataObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.pagingDataFlow.collect {
                    usersAdapter.submitData(it)
                }
            }
        }
    }

    private fun loadStateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                usersAdapter.addLoadStateListener { loadStates ->
                    when (loadStates.refresh) {
                        is LoadState.Error -> {
                            val error: AppError =
                                AppError.fromException((loadStates.refresh as LoadState.Error).error)
                            binding.progressBarUsers.visibility = View.GONE
                            showErrorToast(error)
                        }

                        else -> binding.progressBarUsers.isVisible =
                            loadStates.refresh is LoadState.Loading
                    }
                }
            }
        }
    }

    private fun showErrorToast(error: AppError) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
    }

}