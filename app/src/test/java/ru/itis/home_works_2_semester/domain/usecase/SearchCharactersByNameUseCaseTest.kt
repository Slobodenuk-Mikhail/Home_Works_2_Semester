package ru.itis.home_works_2_semester.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.itis.home_works_2_semester.data.model.CharacterModel
import ru.itis.home_works_2_semester.domain.repository.CharacterRepository

class SearchCharactersByNameUseCaseTest {

    @MockK
    lateinit var repository: CharacterRepository
    
    private lateinit var useCase: SearchCharactersByNameUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SearchCharactersByNameUseCase(repository)
    }

    @Test
    fun `invoke should call repository and return characters`() = runTest {
        // Arrange
        val name = "Rick"
        val expectedList = listOf(
            CharacterModel(1, "Rick Sanchez", "image_url")
        )
        coEvery { repository.searchByName(name) } returns expectedList

        // Act
        val result = useCase(name)

        // Assert
        assertEquals(expectedList, result)
        coVerify(exactly = 1) { repository.searchByName(name) }
    }
}
