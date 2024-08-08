package com.example.notices.data.profileData

import com.example.notices.network.ProfileApiService

interface ProfileRepository{
    suspend fun getProfile(profileRequest: ProfileRequest): Profile
}

interface OfflineProfileRepository{
    suspend fun getProfile():Profile

    suspend fun deleteAll()

    suspend fun insertAll(profile: Profile)
}

class DatabaseProfileRepository(private val profileDao: ProfileDao):OfflineProfileRepository{

    override suspend fun getProfile(): Profile {
        return profileDao.getProfile()
    }

    override suspend fun deleteAll() {
        profileDao.deleteAll()
    }

    override suspend fun insertAll(profile: Profile) {
        profileDao.insertAll(profile)
    }
}

class NetworkProfileRepository(private val apiService : ProfileApiService):ProfileRepository{
    override suspend fun getProfile(profileRequest: ProfileRequest): Profile {
        return apiService.getProfile(profileRequest)
    }
}