package de.lucas.cookingrecipes.auth.presentation.user

import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lucas.cookingrecipes.R
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.auth.domain.user.UserRepository
import de.lucas.cookingrecipes.main.theme.Green
import de.lucas.cookingrecipes.main.theme.Grey
import de.lucas.cookingrecipes.main.theme.Red
import de.lucas.cookingrecipes.main.theme.Yellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState = _userState.asStateFlow()

    var passwordState = mutableStateOf(PasswordState.DEFAULT)
        private set

    init {
        getUserFromDb()
        if (userState.value is UserState.Success) {
            (userState.value as UserState.Success).userData?.id?.let {
                userRepository.setUserDataListener(
                    userId = it,
                    onSuccess = { userData -> _userState.update { UserState.Success(userData) } },
                    onError = { },
                )
            }
        }
    }

    fun loginUser(email: String, password: String, errorMessage: String) = viewModelScope.launch {
        try {
            _userState.update { UserState.Loading }
            delay(1000)
            userRepository.loginUser(
                email = email,
                password = password,
                onSuccess = { userData -> _userState.update { UserState.Success(userData) } },
                onError = { _userState.update { UserState.Error(errorMessage) } },
            )
        } catch (e: Exception) {
            _userState.update { UserState.Error(errorMessage) }
        }
    }

    fun signUpUser(
        email: String,
        password: String,
        username: String,
        errorMessage: String,
    ) = viewModelScope.launch {
        try {
            _userState.update { UserState.Loading }
            delay(1000)
            userRepository.signUpUser(
                email = email,
                password = password,
                username = username,
                onSuccess = { userData -> _userState.update { UserState.Success(userData) } },
                onError = { _userState.update { UserState.Error(errorMessage) } },
            )
        } catch (e: Exception) {
            _userState.update { UserState.Error(errorMessage) }
        }
    }

    fun logoutUser() = viewModelScope.launch {
        userRepository.logoutUser()
        _userState.update { UserState.Idle }
    }

    fun getUserFromDb() = viewModelScope.launch {
        userRepository.getUserFromDb()?.let { userData ->
            _userState.update { UserState.Success(userData) }
        }
    }

    fun resetPassword(email: String) = userRepository.resetPassword(email)

    fun validatePassword(password: String) {
        passwordState.value = when {
            password.length in 1..4 -> PasswordState.VERY_WEAK
            password.length in 5..8 -> PasswordState.WEAK
            password.length in 9..15 -> PasswordState.GOOD
            password.length >= 15 -> PasswordState.STRONG
            else -> PasswordState.DEFAULT
        }
    }
}

sealed class UserState {
    data object Idle : UserState()
    data object Loading : UserState()
    data class Success(val userData: UserData?) : UserState()
    data class Error(val message: String?) : UserState()
}

enum class PasswordState(@StringRes val labelRes: Int, val color: Color, val index: Int) {
    DEFAULT(R.string.passwordStrength, Grey, 0),
    VERY_WEAK(R.string.veryWeak, Red, 1),
    WEAK(R.string.weak, Yellow, 2),
    GOOD(R.string.good, Green, 3),
    STRONG(R.string.strong, Green, 4),
}