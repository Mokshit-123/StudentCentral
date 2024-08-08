package com.example.notices.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.notices.MyApp
import com.example.notices.data.UserPreferencesRepository
import com.example.notices.data.attendanceData.Attendance
import com.example.notices.data.attendanceData.AttendanceRepository
import com.example.notices.data.attendanceData.OfflineAttendanceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.IOException

sealed interface AttendanceUiState {
    data class Success(val attendance: List<Attendance>) : AttendanceUiState
    object Loading : AttendanceUiState
    object Error : AttendanceUiState
}

sealed interface OfflineAttendanceUiState {
    data class Retrieved(val attendance: List<Attendance>) : OfflineAttendanceUiState
    object Loading : OfflineAttendanceUiState
}

class AttendanceViewModel(
    private val attendanceRepository: AttendanceRepository,
    private val offlineAttendanceRepository: OfflineAttendanceRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var attendanceUiState: AttendanceUiState by mutableStateOf(AttendanceUiState.Loading)
        private set

    var offlineAttendanceUiState: OfflineAttendanceUiState by mutableStateOf(OfflineAttendanceUiState.Loading)
        private set

    init {
        fetchEnrollmentNumberAndGetAttendance()
    }

    fun refresh() {
        fetchEnrollmentNumberAndGetAttendance()
    }

    private fun fetchEnrollmentNumberAndGetAttendance() {
        viewModelScope.launch {
            val enrollmentNumber = userPreferencesRepository.username.first() ?: ""
            val token = userPreferencesRepository.loginToken.first()?:""
            if (enrollmentNumber.isNotEmpty()) {
                Log.d("Attendance View model", "fetchEnrollmentNumberAndGetAttendance: $enrollmentNumber")
                getAttendance(enrollmentNumber,token)
            } else {
                Log.e("AttendanceViewModel", "No enrollment number found in DataStore")
                attendanceUiState = AttendanceUiState.Error
            }
        }
    }

    private fun getAttendance(enrollmentNumber: String, token : String) {
        viewModelScope.launch {
            attendanceUiState = AttendanceUiState.Loading

            attendanceUiState = try {
                Log.d("AttendanceViewModel", "getAttendance: fetching attendance")
                val attendanceResult = attendanceRepository.getAttendance(enrollmentNumber,token)
                offlineAttendanceRepository.deleteAll()
                offlineAttendanceRepository.insertAll(attendanceResult)
                AttendanceUiState.Success(
                    attendance = attendanceResult
                )
            } catch (e: IOException) {
                Log.d("AttendanceViewModel", "getAttendance: IOException")
                offlineAttendanceUiState = OfflineAttendanceUiState.Retrieved(offlineAttendanceRepository.getAttendance())
                AttendanceUiState.Error
            } catch (e: HttpException) {
                Log.d("AttendanceViewModel", "getAttendance: HTTPException")
                AttendanceUiState.Error
            } catch (e: Exception) {
                Log.d("AttendanceViewModel", "getAttendance: Exception")
                Log.d("AttendanceViewModel message", "getAttendance : ${e.message}")
                AttendanceUiState.Error
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyApp)
                val attendanceRepository = application.container.attendanceRepository
                val offlineAttendanceRepository = application.container.offlineAttendanceRepository
                val userPreferencesRepository = application.container.userPreferencesRepository
                AttendanceViewModel(attendanceRepository, offlineAttendanceRepository, userPreferencesRepository)
            }
        }
    }
}
