
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notices.ui.ErrorScreen
import com.example.notices.ui.ShimmerLoadingScreen
import com.example.notices.ui.profileScreens.ShowProfile
import com.example.notices.viewModels.OfflineProfileUiState
import com.example.notices.viewModels.ProfileUiState
import com.example.notices.viewModels.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    bottomPaddingValues : PaddingValues = PaddingValues(0.dp)
) {

    Log.d("ProfileScreen", "ProfileScreen: Making viewmodel")
    val profileViewModel : ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
    val profileUiState = profileViewModel.profileUiState
    val offlineProfileUiState = profileViewModel.offlineProfileUiState

    when(profileUiState){
        is ProfileUiState.Loading->{
            Log.d("ProfileScreen", "ProfileScreen: Loading screen")
            ShimmerLoadingScreen(title = "Profile")
        }
        is ProfileUiState.Success->{
            Log.d("ProfileScreen", "ProfileScreen: Loading screen")
            val studentData = profileUiState.studentData
            ShowProfile(
                studentData = studentData,
                onRefresh = { profileViewModel.refresh() },
                bottomPaddingValues = bottomPaddingValues
            )
        }
        is ProfileUiState.Error->{
            if(offlineProfileUiState is OfflineProfileUiState.Retrieved){
                val offlineStudentData = offlineProfileUiState.profileData
                ShowProfile(studentData = offlineStudentData)
                val context = LocalContext.current
                Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }else if(offlineProfileUiState is OfflineProfileUiState.Error){
                val context = LocalContext.current
                Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                ErrorScreen(
                    title = "Profile",
                    onRefresh = { profileViewModel.refresh() }
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen()
}

//@Preview
//@Composable
//private fun Prev() {
//    ProfileScreen(studentData = Profile(
//        enrollmentNumber = "05020803122",
//        name = "Mokshit",
//        course = "B.Tech",
//        session = "2022-2026",
//        profilePhotoLink = "https://media.licdn.com/dms/image/D4E03AQHOnTE-4f30KQ/profile-displayphoto-shrink_100_100/0/1683737106393?e=1727308800&v=beta&t=yHoGNiWCwSd5SssxokkzS7YuOf6TtSdlBynpysAqx7k",
//        phoneNumber = "9971578180",
//        dateOfBirth = "22/01/2003",
//        address = "Bawana, Delhi",
//        emailId = "gargm0068@gmail.com",
//        fatherName = "Manmohan Garg",
//        motherName = "Rajni Garg"
//    ))
//}