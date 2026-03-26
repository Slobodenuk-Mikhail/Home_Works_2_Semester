package ru.itis.home_works_2_semester.data.mapper

import ru.itis.home_works_2_semester.data.model.CharacterDataModel
import ru.itis.home_works_2_semester.data.model.CharacterModel
import ru.itis.home_works_2_semester.network.Character
import ru.itis.home_works_2_semester.network.RnmResponse

class CharacterModelMapper {

    fun mapToModelList(input: RnmResponse): List<CharacterModel>? {
        return input.result?.map { dto ->
            mappingToModel(dto)
        } ?: emptyList()
    }


    fun mapToDataModel(character: Character): CharacterDataModel {
        return mappingToDataModel(character)
    }

    private fun mappingToDataModel(dto: Character): CharacterDataModel {
        return CharacterDataModel(
            remoteId = dto.id.orZero(),
            remoteName = dto.name.orEmpty(),
            remoteStatus = dto.status.orEmpty(),
            remoteSpecies = dto.species.orEmpty(),
            remoteGender = dto.gender.orEmpty(),
            remoteImage = dto.image.orEmpty()
        )
    }

    private fun mappingToModel(dto: Character): CharacterModel {
        return CharacterModel(
            id = dto.id.orZero(),
            name = dto.name.orEmpty(),
            image = dto.image.orEmpty()
        )
    }


    private fun Long?.orZero(): Long = this ?: 0L
    private fun String?.orEmpty(): String = this ?: ""
    private fun List<String>?.orEmpty(): List<String> = this ?: emptyList()
}