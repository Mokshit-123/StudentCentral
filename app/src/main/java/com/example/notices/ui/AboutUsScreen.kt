package com.example.notices.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notices.viewModels.DrawerViewModel
import com.example.notices.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    drawerViewModel: DrawerViewModel = DrawerViewModel(),
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "About Us",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        drawerViewModel.selectedItem("All")
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color(0xFF303F9F)
                )
            )
        }
    ){

        BackHandler {
            drawerViewModel.selectedItem("All")
            navController.popBackStack()
        }

        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color(0xFF303F9F))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mokshit),
                        contentDescription = "DeveloperProfile",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                }

                //Spacer(modifier = Modifier.height(32.dp))

                Box(modifier = Modifier.fillMaxSize()){
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd),
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    ) {
                        val context = LocalContext.current
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://github.com/Mokshit-123")
                        }
                        Column(modifier = modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconBox(icon = Icons.Default.Phone, contentDescription = "Phone Icon")
                                IconBox(
                                    icon = Icons.AutoMirrored.Outlined.Message,
                                    contentDescription = "Message Icon"
                                )
                                IconBox(icon = Icons.Default.Email, contentDescription = "Email Icon")
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp))

                            Text(
                                text = "Contact Information",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                            )

                            ContactInfoRow(label = "Developer:", info = "Mokshit")
                            ContactInfoRow(
                                label = "Github:", info = "Mokshit-123", onClick = {
                                    context.startActivity(intent)
                                })
                            ContactInfoRow(label = "Email:", info = "gargm0068@gmail.com")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconBox(icon: ImageVector, contentDescription: String) {

}

@Composable
fun ContactInfoRow(label: String, info: String, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = info,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAboutUs() {
    AboutUsScreen()
}
