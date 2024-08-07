package com.example.notices.ui.noticesScreens

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notices.R
import com.example.notices.data.noticeData.Notice
import com.example.notices.theme.Montserrat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/*
this function is used to display a list of notices in card format on screen using a lazy column
and on clicking card it shows extra information about the notice
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticesList(
    list: List<Notice>?,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    bottomPadding : PaddingValues = PaddingValues(0.dp),
    onClick: (notice: Notice) -> Unit = { },
    onRefresh:()->Unit = {}
) {

    val isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding()
    ){
        LazyColumn(
            contentPadding = PaddingValues(top = contentPadding.calculateTopPadding(), bottom = bottomPadding.calculateBottomPadding() ),
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            if(list!=null) {
                items(list) { notice ->
                    DisplayNotice(
                        notice = notice,
                        onCardClick = onClick
                    )
                }
            }else{
                item { 
                    Text(text = "No notices found :(")
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



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayNotice(
    notice: Notice,
    onCardClick: (notice: Notice) -> Unit
){
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onCardClick(notice) }
    ) {
        Column(
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                NoticeIcon(noticeIcon = R.drawable.bpit, modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(8.dp))
                NoticeInformation(
                    title = notice.title,
                    description = notice.description,
                    timestamp = notice.timeStamp,
                )
            }
        }
    }
}


@Composable
fun NoticeIcon(
    @DrawableRes noticeIcon : Int,
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(id = noticeIcon),
        contentDescription = null,
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentScale = ContentScale.Fit,

    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeInformation(
    title: String,
    description : String,
    timestamp: String,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            PostedOn(timestamp = timestamp)
        }
        Text(
            text = description,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = TextStyle(
                color = Color.Black,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostedOn(
    timestamp: String
){
    val time = timeOrDate(timestamp)
    Text(
        text = time,
        style = MaterialTheme.typography.bodySmall
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun timeOrDate(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val parsedStamp = LocalDateTime.parse(timestamp, formatter)

    // Define the formatters for time and date
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val fullDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val extractedTime = parsedStamp.format(timeFormatter)
    val extractedDate = parsedStamp.format(dateFormatter)
    val fullDate = parsedStamp.format(fullDateFormatter)

    val currDate = LocalDateTime.now(ZoneId.systemDefault())
    val diff = ChronoUnit.HOURS.between(parsedStamp, currDate)

    return when {
        diff < 24 -> extractedTime
        ChronoUnit.YEARS.between(parsedStamp, currDate) <= 1 -> extractedDate
        else -> fullDate
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search...") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = "Search Icon")
        },
        shape = RoundedCornerShape(8.dp), // Adjust the corner radius as needed
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent, // Remove underline when focused
            unfocusedIndicatorColor = Color.Transparent // Remove underline when not focused
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview()
@Composable
fun DisplayCheck(){
    Card{
        Column{
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                NoticeIcon(noticeIcon = R.drawable.bpit, modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(8.dp))
                NoticeInformation(
                    title = "EXTENDED SCHEDULE OF ENROLMENT FOR CENTRALIZED ONLINE COUNSELLING PROCESS FOR ADMISSION IN THE PROGRAMMES MBA. LLB AND LLM FOR ACADEMIC SESSION 2024-25",
                    description = "This is in continuation to University's Schedule Notification No. 16/2024, IPU-7/DI (Academic)/Online Counselling/2024/150 dated 22.04.2024. The Extended Schedule for Admission in above programmes in the Academic Session 2024-2025. Following category of candidates may Register and submit Online Counselling Participation Fee of Rs.1000/-.",
                    timestamp = "2024-01-07T11:43:08Z"
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSearchBar() {
    SearchBar(query = "Search Here", onQueryChange = { })
}



