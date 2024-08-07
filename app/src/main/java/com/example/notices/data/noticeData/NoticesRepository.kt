package com.example.notices.data.noticeData

import com.example.notices.network.NoticeApiService

//interface that fetch notices
interface NoticesRepository {
    suspend fun getNotices():List<Notice>
}

interface OfflineNoticesRepository{
    suspend fun getAllNotices():List<Notice>

    suspend fun deleteAll()

    suspend fun insertAll(notices:List<Notice>)
}

//following returns notices from database
class DatabaseNoticeRepository(private val noticeDao: NoticeDao) : OfflineNoticesRepository{
    override suspend fun getAllNotices(): List<Notice> {
        return noticeDao.getAllNotices()
    }

    override suspend fun deleteAll() {
        noticeDao.deleteAll()
    }

    override suspend fun insertAll(notices: List<Notice>) {
        noticeDao.insertNotices(notices)
    }


}

//following return notices from web
class NetworkNoticesRepository(private val apiService: NoticeApiService ): NoticesRepository {
    override suspend fun getNotices(): List<Notice> {
        return apiService.getAllNotices()
    }
}
