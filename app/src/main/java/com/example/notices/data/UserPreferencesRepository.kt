package com.example.notices.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val ENROLLMENT_NUMBER_KEY = stringPreferencesKey("enrollment_number")
    }

    val username: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    val enrollmentNumber: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ENROLLMENT_NUMBER_KEY]
    }

    val loginToken : Flow<String?> = dataStore.data.map {preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun updateLoginToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun updateUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            Log.d("UserPreferences", "updateUsername: username updated $username")
        }
    }

    suspend fun updateLogIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    suspend fun updateEnrollmentNumber(enrollmentNumber: String) {
        dataStore.edit { preferences ->
            preferences[ENROLLMENT_NUMBER_KEY] = enrollmentNumber
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
