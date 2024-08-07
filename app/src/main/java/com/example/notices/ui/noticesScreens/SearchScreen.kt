package com.example.notices.ui.noticesScreens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notices.viewModels.DrawerViewModel
import com.example.notices.viewModels.NoticeViewModel
import com.google.gson.Gson
import java.net.URLEncoder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    navController: NavController = rememberNavController(),
    drawerViewModel: DrawerViewModel = DrawerViewModel(),
    noticeViewModel: NoticeViewModel = viewModel(factory = NoticeViewModel.Factory),
    quickQuery : String? = null,
    modifier: Modifier = Modifier
) {

    val searchText by noticeViewModel.searchText.collectAsState()
    val filteredNotices = noticeViewModel.filteredNotices
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(quickQuery) {
        quickQuery?.let {
            noticeViewModel.setQuery(it)
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    BackHandler {
        drawerViewModel.selectedItem("All")
        navController.popBackStack()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = noticeViewModel::setQuery,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { Text(text = "Search...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search icon"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        NoticesList(
            list = filteredNotices,
            onClick = {notice->
                val noticeJson = URLEncoder.encode(Gson().toJson(notice), "UTF-8")
                navController.navigate("detailScreen/$noticeJson")
            }
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PreviewSearchScreen() {
    SearchScreen(quickQuery = null)
}
