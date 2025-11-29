package com.tutorial.rockty.data.mapper

import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.model.Mission
import com.tutorial.rockty.domain.model.Rocket
import com.tutorial.rockty.domain.pagination.PaginatedResult
import com.tutorial.rockty.graphql.LaunchDetailsQuery
import com.tutorial.rockty.graphql.LaunchesQuery

fun LaunchesQuery.Launch.toDomain(): Launch {
    return Launch(
        id = id,
        site = site,
        mission = mission?.let { Mission(it.name, it.missionPatch) },
        rocket = rocket?.let { Rocket(it.name, it.type) }
    )
}

fun LaunchesQuery.Launches.toDomain(): PaginatedResult<Launch> =
    PaginatedResult(
        cursor = cursor,
        hasMore = hasMore,
        items = launches.filterNotNull().map { it.toDomain() }
    )

fun LaunchDetailsQuery.Launch.toDomain(): Launch =
    Launch(
        id = id,
        site = site,
        mission = mission?.let { Mission(it.name, it.missionPatch) },
        rocket = rocket?.let { Rocket(it.name, it.type) }
    )
