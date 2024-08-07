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
import coil.network.HttpException
import com.example.notices.MyApp
import com.example.notices.data.UserPreferencesRepository
import com.example.notices.data.profileData.OfflineProfileRepository
import com.example.notices.data.profileData.Profile
import com.example.notices.data.profileData.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.IOException

sealed interface ProfileUiState {
    data class Success(val studentData: Profile) : ProfileUiState
    object Loading : ProfileUiState
    object Error : ProfileUiState
}

sealed interface OfflineProfileUiState {
    data class Retrieved(val profileData: Profile) : OfflineProfileUiState
    object Loading : OfflineProfileUiState
    object Error : OfflineProfileUiState
}

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val offlineProfileRepository: OfflineProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var profileUiState by mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
        private set

    var offlineProfileUiState by mutableStateOf<OfflineProfileUiState>(OfflineProfileUiState.Loading)
        private set

    init {
        fetchEnrollmentNumberAndGetProfile()
    }

    fun refresh() {
        fetchEnrollmentNumberAndGetProfile()
    }

    private fun fetchEnrollmentNumberAndGetProfile() {
        viewModelScope.launch {
            val enrollmentNumber = userPreferencesRepository.username.first() ?: ""
            if (enrollmentNumber.isNotEmpty()) {
                getProfile(enrollmentNumber)
            } else {
                Log.e("ProfileViewModel", "No enrollment number found in DataStore")
                profileUiState = ProfileUiState.Error
            }
        }
    }

    private fun getProfile(enrollmentNumber: String) {
        viewModelScope.launch {
            profileUiState = ProfileUiState.Loading
            offlineProfileUiState = OfflineProfileUiState.Loading

            profileUiState = try {
                Log.d("ProfileViewModel", "getProfile: fetching profile")
                val profileResult = profileRepository.getProfile(enrollmentNumber)
                offlineProfileRepository.deleteAll()
                offlineProfileRepository.insertAll(profileResult)
                ProfileUiState.Success(studentData = profileResult)
            } catch (e: IOException) {
                Log.d("ProfileViewModel", "getProfile: IOException", e)
                handleOfflineProfile()
            } catch (e: HttpException) {
                Log.d("ProfileViewModel", "getProfile: HTTPException", e)
                handleOfflineProfile()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "getProfile: Exception", e)
                handleOfflineProfile()
            }
        }
    }

    private suspend fun handleOfflineProfile(): ProfileUiState {
        val offlineProfile = offlineProfileRepository.getProfile()
        return if (offlineProfile != null) {
            offlineProfileUiState = OfflineProfileUiState.Retrieved(profileData = offlineProfile)
            ProfileUiState.Error
        } else {
            offlineProfileUiState = OfflineProfileUiState.Error
            ProfileUiState.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApp)
                val profileRepository = application.container.profileRepository
                val offlineProfileRepository = application.container.offlineProfileRepository
                val userPreferencesRepository = application.container.userPreferencesRepository
                ProfileViewModel(
                    profileRepository = profileRepository,
                    offlineProfileRepository = offlineProfileRepository,
                    userPreferencesRepository = userPreferencesRepository
                )
            }
        }
    }
}
