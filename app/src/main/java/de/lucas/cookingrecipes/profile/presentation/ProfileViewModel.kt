package de.lucas.cookingrecipes.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ProfileRepository,
) : ViewModel() {
    private val profile = Profile.from(savedStateHandle)

    private val _user = MutableStateFlow(profile.userData)
    val user = _user.asStateFlow()

    fun saveUserData(userData: UserData) = viewModelScope.launch {
        repository.saveUserData(userData)
        _user.update { userData }
    }
}
