package ru.itis.home_works_2_semester.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.home_works_2_semester.data.model.CharacterDataModel
import ru.itis.home_works_2_semester.domain.repository.CharacterRepository

class GetCharacterByIdUseCase(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke(id: Long): CharacterDataModel {
        return withContext(Dispatchers.IO) {
            characterRepository.searchById(id)
        }
    }
}