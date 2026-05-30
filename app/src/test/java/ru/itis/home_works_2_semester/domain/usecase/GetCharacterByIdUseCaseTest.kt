package ru.itis.home_works_2_semester.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.itis.home_works_2_semester.data.model.CharacterDataModel
import ru.itis.home_works_2_semester.domain.repository.CharacterRepository

class GetCharacterByIdUseCaseTest {

    @MockK
    lateinit var repository: CharacterRepository
    
    private lateinit var useCase: GetCharacterByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetCharacterByIdUseCase(repository)
    }

    @Test
    fun `invoke should return character by id`() = runTest {
        // Arrange
        val id = 1L
        val expectedCharacter = CharacterDataModel(
            remoteId = 1L,
            remoteName = "Rick Sanchez",
            remoteStatus = "Alive",
            remoteSpecies = "Human",
            remoteGender = "Male",
            remoteImage = "image_url"
        )
        coEvery { repository.searchById(id) } returns expectedCharacter

        // Act
        val result = useCase(id)

        // Assert
        assertEquals(expectedCharacter, result)
    }
}
