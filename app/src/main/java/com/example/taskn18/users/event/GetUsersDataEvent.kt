package com.example.taskn18.users.event

sealed class GetUsersDataEvent {
    data object InitialData: GetUsersDataEvent()
    data object RefreshData: GetUsersDataEvent()
}
