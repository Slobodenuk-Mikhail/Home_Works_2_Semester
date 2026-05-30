package ru.itis.home_works_2_semester.domain.repository

import ru.itis.home_works_2_semester.data.model.CharacterDataModel
import ru.itis.home_works_2_semester.data.model.CharacterModel

interface CharacterRepository {

    suspend fun searchByName(name: String): List<CharacterModel>

    suspend fun searchById(id: Long): CharacterDataModel
}