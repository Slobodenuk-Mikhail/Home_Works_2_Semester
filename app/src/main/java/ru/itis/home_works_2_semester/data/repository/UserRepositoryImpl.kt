package ru.itis.home_works_2_semester.data.repository

import ru.itis.home_works_2_semester.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    override fun refreshUserToken(): String {
        return "Sample test string"
    }
}