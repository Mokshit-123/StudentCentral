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
import com.example.notices.data.noticeData.Notice
import com.example.notices.data.noticeData.NoticesRepository
import com.example.notices.data.noticeData.OfflineNoticesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface NoticeUiState{
    data class Success (val notices:List<Notice>): NoticeUiState
    object Error : NoticeUiState
    object Loading : NoticeUiState
}

sealed interface OfflineNoticeUiState{
    data class Retrieved(val notices : List<Notice>) : OfflineNoticeUiState
    object Loading : OfflineNoticeUiState

    object Error:OfflineNoticeUiState
}

class NoticeViewModel(
    private val noticesRepository: NoticesRepository,
    private val offlineNoticesRepository: OfflineNoticesRepository
) : ViewModel() {
    var noticeUiState : NoticeUiState by mutableStateOf(NoticeUiState.Loading)
        private set

    var offlineNoticeUiState : OfflineNoticeUiState by mutableStateOf(OfflineNoticeUiState.Loading)
        private set

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    var filteredNotices : List<Notice>? = mutableListOf()

    fun setQuery(searchQuery : String){
        _searchText.value = searchQuery
        filterNotices(searchQuery)
    }

    private fun filterNotices(query: String) {
        viewModelScope.launch {
            val allNotices = when(offlineNoticeUiState){
                is OfflineNoticeUiState.Retrieved->{
                    (offlineNoticeUiState as OfflineNoticeUiState.Retrieved).notices
                }else->{
                    null
                }
            }
            val results = allNotices?.filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
            }
            filteredNotices = results
        }
    }
    init {
        getNotices()
    }

    fun refreshNotices(){
        getNotices()
    }

    private fun getNotices(){
        viewModelScope.launch {
            noticeUiState = NoticeUiState.Loading
            noticeUiState = try {
                val listResult = noticesRepository.getNotices()
                //starting to delete from database
                offlineNoticesRepository.deleteAll()
                //putting data in database
                offlineNoticesRepository.insertAll(listResult)
                offlineNoticeUiState = OfflineNoticeUiState.Retrieved(
                    notices = listResult
                )
                NoticeUiState.Success(
                    notices = listResult
                )
            }
            catch (e : IOException){
                Log.d("NoticeViewModel:", "getNotices:${e.stackTrace} ")
                val offlineNotices = offlineNoticesRepository.getAllNotices()
                offlineNoticeUiState = OfflineNoticeUiState.Retrieved(
                    notices = offlineNotices
                )
                NoticeUiState.Error
            }
            catch (e : HttpException){
                Log.d("NoticeViewModel:", "getNotices:${e.stackTrace} ")
                val offlineNotices = offlineNoticesRepository.getAllNotices()
                offlineNoticeUiState = OfflineNoticeUiState.Retrieved(
                    notices = offlineNotices
                )
                NoticeUiState.Error

            }
            catch (e : Exception){
                Log.d("NoticeViewModel:", "getNotices:${e.stackTrace} ")
                val offlineNotices = offlineNoticesRepository.getAllNotices()
                offlineNoticeUiState = OfflineNoticeUiState.Retrieved(offlineNotices)
                NoticeUiState.Error
            }
        }
    }
    private suspend fun handleOfflineNotices():NoticeUiState{
        val offlineNotices = offlineNoticesRepository.getAllNotices()
        return if(offlineNotices!=null){
            offlineNoticeUiState = OfflineNoticeUiState.Retrieved(notices = offlineNotices)
            NoticeUiState.Error
        }else{
            offlineNoticeUiState = OfflineNoticeUiState.Error
            NoticeUiState.Error
        }
    }



    //companion object makes a static object which can be used with class name and it belongs to class not instances of class
    //here we are using Factory pattern which lets us overcome the
    //android's limitation of not allowing to pass object to viewmodel at time of creation
    //by using viewModelFactory we create a custom ViewModelProvider.Factory which allows us to pass arguments to viewmodel constructor
    //we use this as follows and create an object as : val noticeViewModel : NoticeViewModel = viewModel(factory = NoticeViewModel.factory)
    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyApp)
                val noticesRepository = application.container.noticesRepository
                val offlineNoticeRepository = application.container.offlineNoticesRepository
                NoticeViewModel(noticesRepository=noticesRepository, offlineNoticesRepository = offlineNoticeRepository)
            }
        }
    }
}