package ru.itis.home_works_2_semester.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.home_works_2_semester.domain.repository.UserRepository

class GeneralExceptionHandlerImpl(
    private val userRepository: UserRepository
) : GeneralExceptionHandler {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun handleException(ex: Throwable) {
        if (ex is HttpException) {
            when (ex.code()) {
                401 -> {
                    scope.launch {
                        val newToken = userRepository.refreshUserToken()
                        println("TEST TAG - Received Token: $newToken")
                    }
                }

                else -> Unit
            }
        }
    }
}