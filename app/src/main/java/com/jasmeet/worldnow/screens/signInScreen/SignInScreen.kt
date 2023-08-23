package com.jasmeet.worldnow.screens.signInScreen

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.ClickableTextComponent
import com.jasmeet.worldnow.appComponents.EmailText
import com.jasmeet.worldnow.appComponents.LoaderComponent
import com.jasmeet.worldnow.appComponents.PasswordFieldComponent
import com.jasmeet.worldnow.appComponents.PasswordText
import com.jasmeet.worldnow.appComponents.TextFieldComponent
import com.jasmeet.worldnow.appComponents.bounceClick
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.utils.rememberImeState
import com.jasmeet.worldnow.viewModels.SignInViewModel

@Composable
fun SignInScreen( ) {

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()


    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background),

            ) {
            MainLayout()
        }
    }
    LaunchedEffect(key1 =imeState.value){
        if(imeState.value){
            scrollState.animateScrollTo(
                scrollState.value + 400,
                animationSpec = tween(1000, easing = EaseInOut)
            )
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.IntroScreen)
    }

}

@Composable
fun MainLayout(loginViewModel: SignInViewModel= hiltViewModel()) {
    val debouncedMessage by loginViewModel.message.collectAsState()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isTermsChecked = remember { mutableStateOf(false) }

    val isLoading = remember {
        mutableStateOf(false)
    }
    val containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val keyboardController = LocalSoftwareKeyboardController.current


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(horizontal = 15.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(top = 10.dp, bottom = 10.dp)
                    .size(135.dp)
                    .border(
                        1.5.dp, Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                Color.Black

                            )
                        ), CircleShape
                    ),
                contentScale = ContentScale.FillBounds

            )

            Text(
                text = "Continue with",
                fontFamily = inter,
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            ElevatedButton(
                onClick = {
                    // TODO("Handle the Google Sign In here")

                },
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 0.dp)
                    .fillMaxWidth()
                    .bounceClick(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 10.dp,

                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(2.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Logo",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Google",
                        fontFamily = inter,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )

                }

            }
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "or",
                fontFamily = inter,
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Login to your Account",
                fontFamily = inter,
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(15.dp))

            EmailText(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )

            TextFieldComponent(
                value = email.value,
                labelValue = "Enter your Email ID",
                onValueChange = {
                    email.value = it
                },
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordText(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )

            PasswordFieldComponent(
                value = password.value,
                labelValue = "Enter your Password",
                onValueChange = {
                    password.value = it
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()) {
                            login(isLoading, loginViewModel, email, password)
                        }
                        else{
                            keyboardController?.hide()
                            loginViewModel.setErrorMessage("Please enter valid email and password")
                        }
                    }
                )
            )

            Text(
                text = "Forgot Password?",
                fontFamily = inter,
                fontSize = 12.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 15.dp)
                    .clickable {
                        //TODO
                    }
                    .padding(10.dp)
                    .bounceClick()
            )
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Checkbox(
                    checked = isTermsChecked.value,
                    onCheckedChange = {
                        isTermsChecked.value = it
                    },
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Remember me",
                    fontFamily = inter,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            ButtonComponent(
                onclick = {
                    login(isLoading, loginViewModel, email, password)
                },
                text = "Login",
                isEnabled = email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty(),
            )

            Spacer(modifier = Modifier.height(15.dp))
            ClickableTextComponent(
                initialText = "Don't have an account?",
                clickableText = "Sign Up",
                onClick = {
                    AppRouter.navigateTo(Screens.SignUpScreen)
                }
            )

        }

        if (debouncedMessage != null && debouncedMessage!!.isNotEmpty()) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(8.dp),
                containerColor = containerColor,
            ) {
                Text(
                    text = debouncedMessage!!,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontWeight = FontWeight(600),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = inter,
                )
            }
        }
    }

    if (isLoading.value || loginViewModel.isLoading.value) {
        LoaderComponent()
    }
}


private fun login(
    isLoading: MutableState<Boolean>,
    loginViewModel: SignInViewModel,
    email: MutableState<String>,
    password: MutableState<String>
) {
    isLoading.value = true
    loginViewModel.signIn(email.value, password.value)
    isLoading.value = false
}
