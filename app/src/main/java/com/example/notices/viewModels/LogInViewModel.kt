package com.example.notices.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notices.MyApp
import com.example.notices.data.UserPreferencesRepository
import com.example.notices.data.logInData.LogInRepository
import com.example.notices.data.logInData.LogInRequest
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object LoggedIn : LoginUiState
    object LoggedOut : LoginUiState
    object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(
    private val logInRepository : LogInRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var loginUiState by mutableStateOf<LoginUiState>(LoginUiState.Loading)
        private set

    init {
        checkLoggedInStatus()
    }

    private fun checkLoggedInStatus() {
        viewModelScope.launch {
            userPreferencesRepository.isLoggedIn.collect { isLoggedIn ->
                loginUiState = if (isLoggedIn) {
                    LoginUiState.LoggedIn
                } else {
                    LoginUiState.LoggedOut
                }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val logInRequest = LogInRequest(username,password)
                val logInResponse = logInRepository.login(logInRequest)
                if(logInResponse.message == "Login successful"){
                    userPreferencesRepository.updateUsername(username)
                    userPreferencesRepository.updateLoginToken(logInResponse.token!!)
                    userPreferencesRepository.updateLogIn(true)
                    loginUiState = LoginUiState.LoggedIn
                }
                else{
                    Log.d("LoginViewModel", "login: ${logInResponse.message}")
                    loginUiState = LoginUiState.Error("Invalid Credentials")
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "login: Exception", e)
                loginUiState = LoginUiState.Error("Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.clearPreferences()
                loginUiState = LoginUiState.LoggedOut
            } catch (e: Exception) {
                Log.e("LoginViewModel", "logout: Exception", e)
                loginUiState = LoginUiState.Error("Logout failed")
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApp)
                val logInRepository = application.container.logInRepository
                val userPreferencesRepository = application.container.userPreferencesRepository
                LoginViewModel(logInRepository,userPreferencesRepository)
            }
        }
    }
}
