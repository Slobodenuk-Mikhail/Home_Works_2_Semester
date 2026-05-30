// SearchViewModel.kt
package ru.itis.home_works_2_semester.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.home_works_2_semester.ServiceLocator
import ru.itis.home_works_2_semester.data.model.CharacterModel
import ru.itis.home_works_2_semester.domain.usecase.SearchCharactersByNameUseCase
import kotlin.reflect.KClass

@Immutable
data class SearchUiState(
    val query: String = "",
    val characterList: List<CharacterModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val searchCharactersByNameUseCase: SearchCharactersByNameUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        // Восстановление сохранённого списка и запроса
        val list = savedStateHandle.get<List<CharacterModel>>("charList") ?: emptyList()
        val query = savedStateHandle.get<String>("query") ?: ""
        _uiState.update { it.copy(characterList = list, query = query) }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        savedStateHandle["query"] = newQuery
    }

    fun searchCharacters() {
        val name = _uiState.value.query
        if (name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = searchCharactersByNameUseCase(name)
                _uiState.update { it.copy(characterList = result, isLoading = false) }
                savedStateHandle["charList"] = result
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error", isLoading = false) }
            }
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: KClass<T>,
                extras: CreationExtras
            ): T {
                val useCase = SearchCharactersByNameUseCase(
                    characterRepository = ServiceLocator.getCharacterRepository()
                )
                return SearchViewModel(
                    searchCharactersByNameUseCase = useCase,
                    savedStateHandle = extras.createSavedStateHandle()
                ) as T
            }
        }
    }
}