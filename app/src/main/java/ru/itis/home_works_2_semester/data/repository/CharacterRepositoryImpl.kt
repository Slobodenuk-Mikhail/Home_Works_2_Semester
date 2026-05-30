package ru.itis.home_works_2_semester.data.repository

import android.util.Log
import ru.itis.home_works_2_semester.data.mapper.CharacterModelMapper
import ru.itis.home_works_2_semester.domain.repository.CharacterRepository
import ru.itis.home_works_2_semester.data.model.CharacterDataModel
import ru.itis.home_works_2_semester.data.model.CharacterModel
import ru.itis.home_works_2_semester.network.RnmApi

class CharacterRepositoryImpl(
    private val rnmApi: RnmApi,
    private val characterModelMapper: CharacterModelMapper
): CharacterRepository {

    override suspend fun searchByName(name: String): List<CharacterModel> {
        Log.d("CharacterRepo", "Searching for name: $name")
        val response = rnmApi.getCharacterByName(name)
        Log.d("CharacterRepo", "Response result size: ${response.result?.size ?: 0}")
        val mapped = characterModelMapper.mapToModelList(response) ?: emptyList()
        Log.d("CharacterRepo", "Mapped size: ${mapped.size}")
        return mapped
    }

    override suspend fun searchById(id: Long): CharacterDataModel {
        val response = rnmApi.getCharacterById(id)
        return characterModelMapper.mapToDataModel(response)
    }

}