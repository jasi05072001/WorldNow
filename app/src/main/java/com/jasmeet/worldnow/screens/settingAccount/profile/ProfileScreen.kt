package com.jasmeet.worldnow.screens.settingAccount.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.EmailText
import com.jasmeet.worldnow.appComponents.Space
import com.jasmeet.worldnow.appComponents.TextFieldComponent
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.utils.rememberImeState

@Composable
fun ProfileScreen() {

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()



    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background),

            ) {
            ProfileScreenLayout()
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenLayout() {

    var photoUri: Uri? by rememberSaveable { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        photoUri = uri

    }

    val userName = rememberSaveable { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }

    val showSuccessDialog = rememberSaveable { mutableStateOf(false) }

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.confetti2)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1.5f,
        restartOnPlay = false

    )


    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Let's get you started",
            fontSize = 25.sp,
            fontFamily = inter,
            fontWeight = FontWeight(900),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Create your Profile",
            fontSize = 20.sp,
            fontFamily = inter,
            fontWeight = FontWeight(800),
            color = MaterialTheme.colorScheme.primary
        )
        Space(height = 10.dp)


        val painter = if (photoUri!= null){
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(photoUri)
                    .crossfade(true)
                    .size(1500)
                    .transformations(CircleCropTransformation())
                    .build()
            )
        }else
        {
            painterResource(id = R.drawable.user)
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .size(110.dp)
                    .aspectRatio(1f)
                    .clickable {
                        launcher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentScale = ContentScale.Fit
            )
            if (photoUri== null) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .offset(x = (-2).dp, y =15.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color(0xB3000000)
                    ),
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(5.dp),

                        )
                }
            }
            else{
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .offset(x = (-2).dp, y = (-80).dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color(0xB3000000)
                    ),
                    onClick = {
                        photoUri = null
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel, contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }

        }

        Space(height =10.dp)

        EmailText(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .fillMaxWidth()
                .align(Alignment.Start),
            text = "UserName"
        )

        TextFieldComponent(
            value = userName.value,
            labelValue = "Enter your Username",
            onValueChange = {
                userName.value = it
            },
            keyboardType = KeyboardType.Text
        )

        Space(height = 10.dp)

        EmailText(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .fillMaxWidth()
                .align(Alignment.Start),
            text = "Name"
        )

        TextFieldComponent(
            value = name.value,
            labelValue = "Enter your Name",
            onValueChange = {
                name.value = it
            },
            keyboardType = KeyboardType.Text
        )
        Space(height = 10.dp)

        EmailText(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .fillMaxWidth()
                .align(Alignment.Start),
            text = "Email"
        )

        TextFieldComponent(
            value = email.value,
            labelValue = "Enter your Email",
            onValueChange = {
                email.value = it
            },
            keyboardType = KeyboardType.Email
        )
        Space(height = 30.dp)

        ButtonComponent(
            onclick = {
                showSuccessDialog.value = true
            },
            text = "Continue",
            isEnabled = validateDetails(userName.value, name.value, email.value)
        )

        Space(height = 10.dp)

    }

    if (showSuccessDialog.value){
        Dialog(onDismissRequest = {
//            showSuccessDialog.value = false
        }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .size(
                        LocalConfiguration.current.screenWidthDp.dp * 0.65f,
                        LocalConfiguration.current.screenHeightDp.dp * 0.4f
                    )
                    .background(
                        Color(0xff359d9e),
                        shape = RoundedCornerShape(MaterialTheme.shapes.medium.topStart)
                    ),

            ){
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds

                )
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.subtract),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp)
                    )
                    Space(height = 10.dp)
                    Text(text ="Congratulations!!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight(900), fontFamily = inter)
                    Space(height = 10.dp)
                    Text(
                        text ="You have \n successfully created \n your account",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight(800),
                        fontFamily = inter,
                        textAlign = TextAlign.Center
                    )
                    Space(height = 10.dp)
                   Button(onClick = { /*TODO*/ },
                       colors = ButtonDefaults.buttonColors(
                           containerColor = Color(0xffcff4d2),
                       )) {
                       Text(text = "Let's Get Started", color = Color(0xff215273), fontSize = 15.sp, fontWeight = FontWeight(800), fontFamily = inter)
                   }
                }


            }
        }
    }




}

fun validateDetails(userName: String, name: String, email: String):Boolean {
    return !(userName.trim().isEmpty() || name.trim().isEmpty() || email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())

}


@Preview(device = Devices.PIXEL_4_XL, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()

}