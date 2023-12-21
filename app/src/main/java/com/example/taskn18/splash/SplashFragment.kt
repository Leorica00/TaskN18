package com.example.taskn18.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.taskn18.BaseFragment
import com.example.taskn18.databinding.FragmentSplashBinding
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun setUp() {
        splashViewModel.onEventHandle(SplashNavigationEvent.goToUsersFragment)
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.splashState.buffer(1, BufferOverflow.DROP_LATEST).collect {
                    when (it) {
                        is GoToOtherFragmentEvent.GoToUsersFragment -> goToUsersFragment()
                    }
                }
            }
        }
    }

    private fun goToUsersFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToUsersFragment())
    }

}