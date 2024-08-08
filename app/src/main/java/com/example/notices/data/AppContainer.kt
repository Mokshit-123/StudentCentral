package com.example.notices.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.notices.data.attendanceData.AttendanceRepository
import com.example.notices.data.attendanceData.DatabaseAttendanceRepository
import com.example.notices.data.attendanceData.NetworkAttendanceRepository
import com.example.notices.data.attendanceData.OfflineAttendanceRepository
import com.example.notices.data.logInData.LogInRepository
import com.example.notices.data.logInData.NetworkLogInRepository
import com.example.notices.data.noticeData.DatabaseNoticeRepository
import com.example.notices.data.noticeData.NetworkNoticesRepository
import com.example.notices.data.noticeData.NoticesRepository
import com.example.notices.data.noticeData.OfflineNoticesRepository
import com.example.notices.data.profileData.DatabaseProfileRepository
import com.example.notices.data.profileData.NetworkProfileRepository
import com.example.notices.data.profileData.OfflineProfileRepository
import com.example.notices.data.profileData.ProfileRepository
import com.example.notices.network.AttendanceApiService
import com.example.notices.network.LogInApiService
import com.example.notices.network.NoticeApiService
import com.example.notices.network.ProfileApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private const val PREFERENCES_NAME = "user_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

interface AppContainer {
    val noticesRepository: NoticesRepository
    val offlineNoticesRepository: OfflineNoticesRepository
    val attendanceRepository: AttendanceRepository
    val offlineAttendanceRepository: OfflineAttendanceRepository
    val profileRepository: ProfileRepository
    val offlineProfileRepository: OfflineProfileRepository
    val logInRepository: LogInRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(context: Context) : AppContainer {


    private val BASE_URL ="Enter BASE_URL here"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val noticeRetrofitService: NoticeApiService by lazy {
        retrofit.create(NoticeApiService::class.java)
    }
    override val noticesRepository: NoticesRepository by lazy {
        NetworkNoticesRepository(noticeRetrofitService)
    }
    override val offlineNoticesRepository: OfflineNoticesRepository by lazy {
        DatabaseNoticeRepository(AppDatabase.getDatabase(context).noticeDao())
    }

    private val attendanceRetrofitService: AttendanceApiService by lazy {
        retrofit.create(AttendanceApiService::class.java)
    }
    override val attendanceRepository: AttendanceRepository by lazy {
        NetworkAttendanceRepository(attendanceRetrofitService)
    }
    override val offlineAttendanceRepository: OfflineAttendanceRepository by lazy {
        DatabaseAttendanceRepository(AppDatabase.getDatabase(context).attendanceDao())
    }

    private val profileRetrofitService: ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }
    override val profileRepository: ProfileRepository by lazy {
        NetworkProfileRepository(profileRetrofitService)
    }
    override val offlineProfileRepository: OfflineProfileRepository by lazy {
        DatabaseProfileRepository(AppDatabase.getDatabase(context).profileDao())
    }

    private val loginRetrofitService: LogInApiService by lazy {
        retrofit.create(LogInApiService::class.java)
    }
    override val logInRepository: LogInRepository by lazy {
        NetworkLogInRepository(loginRetrofitService)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
}
