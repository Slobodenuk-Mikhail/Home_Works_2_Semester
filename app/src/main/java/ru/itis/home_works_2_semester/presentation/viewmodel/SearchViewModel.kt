// SearchViewModel.kt
package ru.itis.home_works_2_semester.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.home_works_2_semester.ServiceLocator
import ru.itis.home_works_2_semester.data.model.CharacterModel
import ru.itis.home_works_2_semester.domain.usecase.SearchCharactersByNameUseCase
import kotlin.reflect.KClass

class SearchViewModel(
    private val searchCharactersByNameUseCase: SearchCharactersByNameUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _characterList = MutableStateFlow<List<CharacterModel>>(emptyList())
    val characterList: StateFlow<List<CharacterModel>> = _characterList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Восстановление сохранённого списка
        savedStateHandle.get<List<CharacterModel>>("charList")?.let {
            _characterList.value = it
        }
    }

    fun searchCharacters(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = searchCharactersByNameUseCase(name)
                _characterList.value = result
                savedStateHandle["charList"] = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
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