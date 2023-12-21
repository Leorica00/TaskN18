package com.example.taskn18.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private var _splashState = MutableSharedFlow<GoToOtherFragmentEvent>(1)
    val splashState = _splashState.asSharedFlow()

    fun onEventHandle(event: SplashNavigationEvent) {
        when (event) {
            is SplashNavigationEvent.goToUsersFragment -> goToUsersFragment()
        }
    }

    private fun goToUsersFragment() {
        viewModelScope.launch {
            delay(2000)
            _splashState.emit(GoToOtherFragmentEvent.GoToUsersFragment)
        }
    }
}

sealed class GoToOtherFragmentEvent {
    data object GoToUsersFragment : GoToOtherFragmentEvent()
}