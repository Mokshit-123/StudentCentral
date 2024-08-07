package com.example.notices.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notices.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    logInViewModel: LoginViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordMasked by remember { mutableStateOf(true) }

    var isUsernameValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isLoginEnabled by remember { mutableStateOf(false) }
    var usernameChanged by remember { mutableStateOf(false) }
    var passwordChanged by remember { mutableStateOf(false) }

    fun validateUsername(username: String) {
        isUsernameValid = username.length == 11 && username.all { it.isDigit() }
    }
    fun validatePassword(password: String) {
        isPasswordValid = password.isNotEmpty()
    }

    LaunchedEffect(username, password) {
        validateUsername(username)
        validatePassword(password)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ){
        Column (
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "logo",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    validateUsername(it)
                    usernameChanged = true
                    isLoginEnabled = isUsernameValid && isPasswordValid
                                },
                label = {
                    Text(text = "Username")
                },
                isError = usernameChanged && isUsernameValid,
                singleLine = true
            )
           if(usernameChanged && !isUsernameValid){
               Text(
                   text = "Username must be an 11 digit number",
                   color = Color.Red,
                   modifier = Modifier.align(Alignment.Start)
               )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange ={
                    password = it
                    validatePassword(it)
                    passwordChanged = true
                    isLoginEnabled = isUsernameValid && isPasswordValid
                },
                label = {
                    Text(text = "Password")
                },
                isError = passwordChanged && isPasswordValid,
                trailingIcon = {
                               Icon(
                                   imageVector = if(isPasswordMasked) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                   contentDescription = if(isPasswordMasked) "Show Password" else " Hide Password",
                                   modifier=Modifier
                                       .clickable {
                                           isPasswordMasked = !isPasswordMasked
                                       }
                               )
                },
                singleLine = true,
                visualTransformation = if(isPasswordMasked) PasswordVisualTransformation() else VisualTransformation.None
            )
            if(passwordChanged && !isPasswordValid){
                Text(
                    text = "Password cannot be empty!",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                          logInViewModel.login(username,password)
                },
                enabled = isLoginEnabled
            ) {
                Text(text = "Submit")
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewLogInScreen() {
    LoginScreen()
}