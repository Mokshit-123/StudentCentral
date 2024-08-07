package com.example.notices.ui.noticesScreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.notices.viewModels.NoticeViewModel

@Composable
fun QuickSearchScreen(
    noticeViewModel: NoticeViewModel,
    searchItem:String,
    modifier: Modifier = Modifier
) {
    noticeViewModel.setQuery(searchItem)
    val noticesList = noticeViewModel.filteredNotices

}