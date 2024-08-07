package com.example.notices.ui.attendanceScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notices.data.attendanceData.Attendance
import com.example.notices.ui.DonutPieChart
import com.example.notices.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OverallAttendance(
    attendanceList : List<Attendance>,
    contentPadding: PaddingValues,
    onRefresh:()->Unit = { }
) {

    val isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding()
    ){
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .pullRefresh(pullRefreshState)
        ) {

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    DonutPieChart(
                        attendanceList = attendanceList
                    )
                }
            }
            items(attendanceList) {
                DisplayAttendance(attendance = it)
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun DisplayAttendance(
    attendance: Attendance,
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.notice_provider),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = attendance.subjectName,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Faculty : ${attendance.subjectTeacherName}",
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Classes Attended: ${attendance.attendedClasses} / ${attendance.totalClasses}",
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewDisplayAttendance() {
    val dummyAttendance = Attendance("Dummy Subject Name", "Dummy Teacher Name",10, 8)
    DisplayAttendance(attendance = dummyAttendance)
}

@Preview
@Composable
private fun PreviewOverallAttendance() {
    val attendanceList = listOf(
        Attendance("Math", "Mr. A", 50, 45),
        Attendance("Science", "Ms. B", 50, 40),
        Attendance("History", "Mr. C", 50, 35),
        Attendance("English", "Ms. D", 50, 30),
        Attendance("Geography", "Mr. E", 50, 25),
        Attendance("Physics", "Mr. F", 50, 45),
        Attendance("Chemistry", "Ms. G", 50, 40),
        Attendance("Biology", "Mr. H", 50, 35)
    )
    Scaffold {
        OverallAttendance(attendanceList = attendanceList, contentPadding = it)
    }
}