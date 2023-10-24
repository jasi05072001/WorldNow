package com.jasmeet.worldnow.screens.onBoarding.signUpScreen


import android.content.Context
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.CircleImageView
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
import com.jasmeet.worldnow.rememberFirebaseAuthLauncher
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.utils.rememberImeState
import com.jasmeet.worldnow.utils.saveUserInfo
import com.jasmeet.worldnow.utils.saveUserInfoToSharedPrefs
import com.jasmeet.worldnow.viewModels.SignUpViewModel

@Composable
fun SignUpScreen( ) {
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
                scrollState.maxValue,
                animationSpec = tween(1000, easing = EaseInOut)
            )
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.IntroScreen)
    }

}

@Composable
private fun MainLayout(signUpViewModel: SignUpViewModel = hiltViewModel()) {

    val debouncedMessage by signUpViewModel.message.collectAsState()
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val isLoading = rememberSaveable {
        mutableStateOf(false)
    }
    val containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val keyboardController = LocalSoftwareKeyboardController.current

    val token = stringResource(R.string.default_web_client_id)
    val gso = remember{
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .requestProfile()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val user = remember { mutableStateOf(Firebase.auth.currentUser) }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = {result ->
            user.value = result.user
            saveUserInfo(context)
            saveUserInfoToSharedPrefs(user.value?.email.toString(),context)
            isLoading.value = false

        },
        onAuthError = {
            user.value = null
            isLoading.value = false
            signUpViewModel.setErrorMessage(it)

        }
    )


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(horizontal = 15.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            CircleImageView(imgRes = R.drawable.logo)

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
                    isLoading.value = true
                    launcher.launch(googleSignInClient.signInIntent)

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
                text = "Create your Account",
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
                    .align(Alignment.Start),
                text = "Email"
            )

            TextFieldComponent(
                value = email.value,
                labelValue = "Enter your Email ID",
                onValueChange = {
                    email.value = it
                    AppRouter.email = email.value
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
                            signUp(isLoading, signUpViewModel, email, password,context)
                        }
                        else{
                            keyboardController?.hide()
                            signUpViewModel.setErrorMessage("Please enter valid email and password")
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            ButtonComponent(
                onclick = {
                    signUp(isLoading, signUpViewModel, email, password ,context)
                    AppRouter.email = email.value
                },
                text = "Sign up",
                isEnabled = email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty(),
            )

            Spacer(modifier = Modifier.height(15.dp))
            ClickableTextComponent(
                initialText = "Already have an account?",
                clickableText = "Login",
                onClick = {
                    AppRouter.navigateTo(Screens.SignInScreen)
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            if (debouncedMessage != null && debouncedMessage!!.isNotEmpty()) {
                Snackbar(
                    modifier = Modifier
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
    }

    if (isLoading.value || signUpViewModel.isLoading.value) {
        LoaderComponent()
    }
}




private fun signUp(
    isLoading: MutableState<Boolean>,
    loginViewModel: SignUpViewModel,
    email: MutableState<String>,
    password: MutableState<String>,
    context: Context
) {
    isLoading.value = true
    loginViewModel.signUp(email.value, password.value)
    saveUserInfoToSharedPrefs(email.value,context)
    isLoading.value = false
}