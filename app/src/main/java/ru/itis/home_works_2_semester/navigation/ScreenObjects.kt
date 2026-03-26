package ru.itis.home_works_2_semester.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SearchScreenObject {const val route = "search"}

@Serializable
data object InfoScreenObject {
    const val route = "info/{id}"
    fun createRoute(id: Long) = "info/$id"
}
