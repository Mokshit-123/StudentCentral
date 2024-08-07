package com.example.notices.ui.noticesScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.notices.data.noticeData.Notice
import com.example.notices.ui.PdfViewer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeDetails(
    notice: Notice,
    bottomPadding : PaddingValues = PaddingValues(0.dp)
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val parsedStamp = LocalDateTime.parse(notice.timeStamp, formatter)

    // Define the formatters for time and date
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val fullDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val extractedTime = parsedStamp.format(timeFormatter)
    val fullDate = parsedStamp.format(fullDateFormatter)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(bottomPadding)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val textFormats = listOf(
                TextFormat(
                    text = notice.title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    padding = 8.dp
                ),
                TextFormat(
                    text = "Date: $fullDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    padding = 4.dp
                ),
                TextFormat(
                    text = "Time: $extractedTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    padding = 16.dp
                ),
                TextFormat(
                    text = "Description",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    padding = 4.dp
                ),
                TextFormat(
                    text = notice.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    padding = 16.dp
                )
            )

            LazyColumn {
                items(textFormats) {
                    DisplayNoticeItems(formatting = it)
                }
                item {
                    Box(modifier = Modifier.height(512.dp)){
                        ComposePDFViewerFromUrl(pdfUrl = notice.pdfURL)
                    }
                }

            }
        }
    }
}

data class TextFormat(
    val text: String,
    val style: androidx.compose.ui.text.TextStyle,
    val color: Color,
    val padding: Dp
)

@Composable
fun DisplayNoticeItems(
    formatting: TextFormat
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = formatting.padding)
    ) {
        BasicText(
            text = formatting.text,
            style = formatting.style,
            modifier = Modifier
                .animateContentSize()
                .clickable { expanded = !expanded }
                .then(
                    if (!expanded) Modifier else Modifier.heightIn(max = Int.MAX_VALUE.dp)
                ),
            overflow = TextOverflow.Ellipsis,
            maxLines = if (expanded) Int.MAX_VALUE else 3
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun ComposePDFViewerFromUrl(pdfUrl: String) {
    var isLoading by remember { mutableStateOf(false) }
    var currentLoadingPage by remember { mutableStateOf<Int?>(null) }
    var pageCount by remember { mutableStateOf<Int?>(null) }
    var pdfInputStream by remember { mutableStateOf<InputStream?>(null) }

    LaunchedEffect(pdfUrl) {
        isLoading = true
        pdfInputStream = downloadPdfFromUrl(pdfUrl)
        isLoading = false
    }

    Box {
        pdfInputStream?.let {
            PdfViewer(
                modifier = Modifier.fillMaxSize(),
                pdfStream = it,
                loadingListener = { loading, currentPage, maxPage ->
                    isLoading = loading
                    if (currentPage != null) currentLoadingPage = currentPage
                    if (maxPage != null) pageCount = maxPage
                }
            )
        }
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    progress = if (currentLoadingPage == null || pageCount == null) 0f
                    else currentLoadingPage!!.toFloat() / pageCount!!.toFloat()
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 5.dp)
                        .padding(horizontal = 30.dp),
                    text = "${currentLoadingPage ?: "-"} pages loaded/${pageCount ?: "-"} total pages"
                )
            }
        }
    }
}

suspend fun downloadPdfFromUrl(pdfUrl: String): InputStream? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(pdfUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            connection.inputStream
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ComposePDFViewer() {
//    var isLoading by remember { mutableStateOf(false) }
//    var currentLoadingPage by remember { mutableStateOf<Int?>(null) }
//    var pageCount by remember { mutableStateOf<Int?>(null) }
//
//    Box {
//        PdfViewer(
//            modifier = Modifier.fillMaxSize(),
//            pdfResId = R.raw.samplepdf,
//            loadingListener = { loading, currentPage, maxPage ->
//                isLoading = loading
//                if (currentPage != null) currentLoadingPage = currentPage
//                if (maxPage != null) pageCount = maxPage
//            }
//        )
//        if (isLoading) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center
//            ) {
//                LinearProgressIndicator(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 30.dp),
//                    progress = if (currentLoadingPage == null || pageCount == null) 0f
//                    else currentLoadingPage!!.toFloat() / pageCount!!.toFloat()
//                )
//                Text(
//                    modifier = Modifier
//                        .align(Alignment.End)
//                        .padding(top = 5.dp)
//                        .padding(horizontal = 30.dp),
//                    text = "${currentLoadingPage ?: "-"} pages loaded/${pageCount ?: "-"} total pages"
//                )
//            }
//        }
//    }
//}