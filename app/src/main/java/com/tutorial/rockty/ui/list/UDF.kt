package com.tutorial.rockty.ui.list

sealed interface LaunchListIntent {
    object LoadMore : LaunchListIntent
    object Refresh : LaunchListIntent
    data class OnLaunchClicked(val id: String) : LaunchListIntent

}
sealed interface LaunchListEffect {
    data class NavigateToDetail(val id: String) : LaunchListEffect
}