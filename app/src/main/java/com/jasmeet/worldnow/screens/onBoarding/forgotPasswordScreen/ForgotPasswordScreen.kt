package com.jasmeet.worldnow.screens.onBoarding.forgotPasswordScreen

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.CircleImageView
import com.jasmeet.worldnow.appComponents.EmailText
import com.jasmeet.worldnow.appComponents.LoaderComponent
import com.jasmeet.worldnow.appComponents.SuccessLottie
import com.jasmeet.worldnow.appComponents.TextFieldComponent
import com.jasmeet.worldnow.appComponents.bounceClick
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.ui.theme.workSans
import com.jasmeet.worldnow.utils.rememberImeState
import com.jasmeet.worldnow.viewModels.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(loginViewModel: SignInViewModel = hiltViewModel()) {

    val color = if(isSystemInDarkTheme()) Color.White else Color.Black

    val debouncedMessage by loginViewModel.message.collectAsState()
    val email = remember { mutableStateOf("") }

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    val isLoading = remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val resetPasswordState = loginViewModel.stateFlow.collectAsState()



    DisposableEffect(key1 = Unit) {
        onDispose {
            loginViewModel.resetResetPasswordState()
        }
    }

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.SignInScreen)
    }
    LaunchedEffect(key1 =imeState.value){
        if(imeState.value){
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(1000, easing = EaseInOut)
            )
        }
    }


    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .height(40.dp),
                title = {},
                navigationIcon = {
                    if (!resetPasswordState.value)
                        IconButton(
                            onClick = {
                                AppRouter.navigateTo(Screens.SignInScreen)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                tint = color
                            )
                        }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }

    ){
        if (!resetPasswordState.value) {
            ResetPasswordUi(
                it,
                scrollState,
                color,
                email,
                loginViewModel,
                isLoading,
                keyboardController,
                debouncedMessage,
                containerColor
            )
        }
        if (isLoading.value || loginViewModel.isLoading.value) {
            LoaderComponent()
        }


        if (resetPasswordState.value){
            SuccessUi(color,it)
        }

    }
}

@Composable
private fun SuccessUi(color: Color, paddingValues: PaddingValues) {

    Column (
        modifier = Modifier
            .padding(paddingValues.calculateTopPadding() - 15.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SuccessLottie()
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Reset successful",
            fontFamily = workSans,
            fontWeight = FontWeight.W600,
            fontSize = 25.sp,
            color = color

        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "You can now log in to your account after resetting your password.",
            color = color,
            fontWeight = FontWeight(600),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontFamily = inter,
        )

        Spacer(modifier = Modifier.weight( 1f, true))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .padding(10.dp)
                .bounceClick()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    AppRouter.navigateTo(Screens.SignInScreen)
                }

        ){
            Text(
                text = "Login",
                color = color,
                fontWeight = FontWeight(600),
                fontFamily = inter,
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }

    }


}

@Composable
private fun ResetPasswordUi(
    it: PaddingValues,
    scrollState: ScrollState,
    color: Color,
    email: MutableState<String>,
    loginViewModel: SignInViewModel,
    isLoading: MutableState<Boolean>,
    keyboardController: SoftwareKeyboardController?,
    debouncedMessage: String?,
    containerColor: Color
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(it.calculateTopPadding() - 15.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircleImageView(
            imgRes = R.drawable.img_forgot_password,
            size = DpSize(200.dp, 200.dp),
            contentDescription = "Forgot Password illustration"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Forgot Password?",
            fontFamily = workSans,
            fontWeight = FontWeight.W600,
            fontSize = 25.sp,
            color = color

        )

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Don’t worry, it happens to the best of us.",
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = workSans,
                fontWeight = FontWeight(400),
                color = color,
                textAlign = TextAlign.Center,
            )
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
            onValueChange = { value ->
                email.value = value
            },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (email.value.trim().isNotEmpty()) {
                        reset(email, loginViewModel, isLoading)
                    } else {
                        keyboardController?.hide()
                        loginViewModel.setErrorMessage("Please enter email")
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(25.dp))

        ButtonComponent(
            onclick = {
                reset(email, loginViewModel, isLoading)
            },
            text = "Continue",
            isEnabled = email.value.trim().isNotEmpty(),
        )

        if (!debouncedMessage.isNullOrEmpty() ) {
            Snackbar(
                modifier = Modifier
                    .padding(top = 45.dp)
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(8.dp),
                containerColor = containerColor,
            ) {
                Text(
                    text = debouncedMessage,
                    color = color,
                    fontWeight = FontWeight(600),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = inter,
                )
            }
        }
    }
}

fun reset(
    email: MutableState<String>,
    loginViewModel: SignInViewModel,
    loading: MutableState<Boolean>)
{
    loading.value = true
    loginViewModel.resetPasswordLink(email.value)
    loading.value = false



}
