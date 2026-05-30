package ru.itis.home_works_2_semester.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import ru.itis.home_works_2_semester.data.model.CharacterModel
import ru.itis.home_works_2_semester.domain.usecase.SearchCharactersByNameUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @MockK
    lateinit var searchUseCase: SearchCharactersByNameUseCase
    
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(searchUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onQueryChanged should update query in uiState`() {
        // Arrange
        val newQuery = "Rick"

        // Act
        viewModel.onQueryChanged(newQuery)

        // Assert
        assertEquals(newQuery, viewModel.uiState.value.query)
        assertEquals(newQuery, savedStateHandle.get<String>("query"))
    }

    @Test
    fun `searchCharacters should update uiState with character list on success`() = runTest {
        // Arrange
        val name = "Morty"
        viewModel.onQueryChanged(name)
        val expectedList = listOf(CharacterModel(2, "Morty Smith", "url"))
        coEvery { searchUseCase(name) } returns expectedList

        // Act
        viewModel.searchCharacters()

        // Assert
        val currentState = viewModel.uiState.value
        assertEquals(expectedList, currentState.characterList)
        assertFalse(currentState.isLoading)
        assertEquals(null, currentState.error)
    }

    @Test
    fun `searchCharacters should update uiState with error on failure`() = runTest {
        // Arrange
        val name = "Rick"
        viewModel.onQueryChanged(name)
        val errorMessage = "Network Error"
        coEvery { searchUseCase(name) } throws Exception(errorMessage)

        // Act
        viewModel.searchCharacters()

        // Assert
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertEquals(errorMessage, currentState.error)
    }
}
