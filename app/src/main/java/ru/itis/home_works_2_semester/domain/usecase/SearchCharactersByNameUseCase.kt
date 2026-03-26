package ru.itis.home_works_2_semester.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.home_works_2_semester.domain.repository.CharacterRepository
import ru.itis.home_works_2_semester.data.model.CharacterModel

class SearchCharactersByNameUseCase(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke(name: String): List<CharacterModel> {
        return withContext(Dispatchers.IO) {
            characterRepository.searchByName(name)
        }
    }
}