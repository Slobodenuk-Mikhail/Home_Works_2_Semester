package ru.itis.home_works_2_semester.domain.repository

interface UserRepository {

    fun refreshUserToken(): String
}