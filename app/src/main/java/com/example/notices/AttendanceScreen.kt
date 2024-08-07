package com.example.notices

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notices.ui.LoadingScreen
import com.example.notices.ui.noticesScreens.TopBar
import com.example.notices.viewModels.AttendanceUiState
import com.example.notices.viewModels.AttendanceViewModel
import com.example.notices.viewModels.OfflineAttendanceUiState

@Composable
fun AttendanceScreen(
    bottomPaddingValues:PaddingValues
){
    Surface(
        modifier = Modifier.fillMaxSize()
    ){

        Log.d("Inside OnScreen", "AttendanceScreen: making viewmodel")
        val attendanceViewModel : AttendanceViewModel = viewModel(factory = AttendanceViewModel.factory)
        val attendanceUiState = attendanceViewModel.attendanceUiState
        val offlineAttendanceUiState = attendanceViewModel.offlineAttendanceUiState

        Log.d("Inside OnScreen", "AttendanceScreen: checking with when")
        when(attendanceUiState){
            is AttendanceUiState.Loading -> {
                LoadingScreen(title = "Attendance")
            }
            is AttendanceUiState.Success->{
                Log.d("Inside OnScreen", "AttendanceScreen: Successfully retrieved attendance")
                val attendanceRetrieved = attendanceUiState.attendance
                TopBar(
                    title = "Attendance",
                    attendanceList = attendanceRetrieved,
                    bottomPaddingValues = bottomPaddingValues,
                    onRefresh = { attendanceViewModel.refresh() }
                )
            }
            is AttendanceUiState.Error -> {

                if( offlineAttendanceUiState is OfflineAttendanceUiState.Retrieved){
                    val context = LocalContext.current
                    Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                    TopBar(
                        title = "Attendance",
                        bottomPaddingValues = bottomPaddingValues,
                        attendanceList = offlineAttendanceUiState.attendance,
                        onRefresh = { attendanceViewModel.refresh() }
                    )
                }
            }
        }

    }
}