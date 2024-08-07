package com.example.notices.ui.profileScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.notices.data.profileData.Profile

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ShowProfile(
    studentData:Profile,
    onRefresh :()->Unit = {},
    bottomPaddingValues : PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    val isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)
    Scaffold(
        topBar = {
            TopAppBar(
                title =
                {
                    Text(
                        text = "Hey, ${studentData.name}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding()
        ){
            LazyColumn(
                contentPadding = PaddingValues(top = it.calculateTopPadding(), bottom = bottomPaddingValues.calculateBottomPadding()),
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
                state = scrollState,
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(studentData.profilePhotoLink),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                ProfileDetail("Enrollment Number", studentData.enrollmentNumber)
                                ProfileDetail("Course", studentData.course)
                                ProfileDetail("Session", studentData.session)
                                ProfileDetail("Phone Number", studentData.phoneNumber)
                                ProfileDetail("Date of Birth", studentData.dateOfBirth)
                                ProfileDetail("Address", studentData.address)
                                ProfileDetail("Email ID", studentData.emailId)
                                if (studentData.fatherName != null) {
                                    ProfileDetail("Father's Name", studentData.fatherName)
                                } else if (studentData.motherName != null) {
                                    ProfileDetail("Mother's Name", studentData.motherName)
                                }
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
@Composable
fun ProfileDetail(label: String, value: String) {
    Column() {
        Text(text = label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(start = 12.dp, top = 12.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 12.dp))
    }
}

@Preview
@Composable
private fun PreviewProfileDetail() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileDetail(label = "Sample Item", value = "Sample Detail of Item")

    }
}

@Preview
@Composable
private fun PreviewProfileScreen() {
    ShowProfile(
        studentData = Profile(
            enrollmentNumber = "05020803122",
            name = "Mokshit",
            course = "B.Tech",
            session = "2022-26",
            profilePhotoLink = "",
            phoneNumber = "9971578180",
            dateOfBirth = "22-01-2003",
            emailId = "gargm0068@gmail.com",
            address = "Bawana, Delhi",
            fatherName = "Manmohan Garg",
            motherName = "Rajni Garg"
        )
    )
}