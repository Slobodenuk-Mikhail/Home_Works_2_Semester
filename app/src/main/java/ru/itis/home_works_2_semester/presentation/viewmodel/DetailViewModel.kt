// DetailViewModel.kt
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
import ru.itis.home_works_2_semester.data.model.CharacterDataModel
import ru.itis.home_works_2_semester.domain.usecase.GetCharacterByIdUseCase
import kotlin.reflect.KClass

class DetailViewModel(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _character = MutableStateFlow<CharacterDataModel?>(null)
    val character: StateFlow<CharacterDataModel?> = _character.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val characterId: Long = savedStateHandle.get<String>("id")?.toLongOrNull() ?: 0L

    init {
        savedStateHandle.get<CharacterDataModel?>("character")?.let {
            _character.value = it
        } ?: loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = getCharacterByIdUseCase(characterId)
                _character.value = result
                savedStateHandle["character"] = result
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
                val useCase = GetCharacterByIdUseCase(
                    characterRepository = ServiceLocator.getCharacterRepository()
                )
                return DetailViewModel(
                    getCharacterByIdUseCase = useCase,
                    savedStateHandle = extras.createSavedStateHandle()
                ) as T
            }
        }
    }
}