package com.example.notices.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimmerLoadingScreen(
    title:String,
    bottomPaddingValues: PaddingValues = PaddingValues(0.dp)
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) {
        LazyColumn (
            contentPadding = PaddingValues(top = it.calculateTopPadding(), bottom = bottomPaddingValues.calculateBottomPadding())
        ){
            when (title){
                "Notice"-> {
                    items(20){
                        NoticeItemShimmer()
                    }
                }
                "Attendance"-> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                        }
                    }
                    items(20){
                        AttendanceItemShimmer()
                    }
                }
                "Profile"->{
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                        }
                    }
                    item{
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ){
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Column(modifier = Modifier.padding(bottom = 12.dp)){
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                    ProfileItemShimmer()
                                }

                            }
                        }
                        
                    }
                }
            }
        }
            
    }
}

@Composable
fun NoticeItemShimmer(){
    Card (
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .padding(8.dp)
    ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                //verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(22.dp)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(12.dp)
                            .shimmerEffect()
                    )
                }
            }
        }

    }

}

@Composable
fun AttendanceItemShimmer(){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
                .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ProfileItemShimmer() {
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Box(modifier = Modifier
            .fillMaxWidth(0.4f)
            .height(30.dp)
            .padding(start = 12.dp, top = 12.dp)
            .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(22.dp)
            .padding(start = 12.dp)
            .shimmerEffect()
        )
    }

}
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Preview
@Composable
private fun PreviewNoticeShimmerItemPreview() {
    NoticeItemShimmer()
}

@Preview
@Composable
private fun PreviewAttendanceShimmerItemPreview() {
    AttendanceItemShimmer()
}


@Preview
@Composable
private fun PreviewProfileShimmerItemPreview() {
    ProfileItemShimmer()
}
@Preview
@Composable
private fun PreviewNoticeShimmerLoadingScreen() {
    ShimmerLoadingScreen(title = "Notice")
}

@Preview
@Composable
private fun PreviewAttendanceShimmerLoadingScreen() {
    ShimmerLoadingScreen(title = "Attendance")
}

@Preview
@Composable
private fun PreviewProfileShimmerLoadingScreen() {
    ShimmerLoadingScreen(title = "Profile")
}