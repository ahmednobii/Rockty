package com.tutorial.rockty.domain.model


data class LaunchList(
    val cursor: String?,
    val hasMore: Boolean,
    val launches: List<Launch?>
)

data class Launch(
    val id: String,
    val site: String?,
    val mission: Mission?,
    val rocket: Rocket?
)

data class Mission(
    val name: String?,
    val missionPatch: String?
)

data class Rocket(
    val name: String?,
    val type: String?
)
