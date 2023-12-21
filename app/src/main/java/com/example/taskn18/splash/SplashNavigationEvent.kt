package com.example.taskn18.splash

sealed class SplashNavigationEvent{
    data object goToUsersFragment : SplashNavigationEvent()
}
