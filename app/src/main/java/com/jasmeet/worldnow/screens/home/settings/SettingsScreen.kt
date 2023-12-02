package com.jasmeet.worldnow.screens.home.settings

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.Space
import com.jasmeet.worldnow.appComponents.bounceClick
import com.jasmeet.worldnow.dataStore.DataStoreUtil
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.utils.showToast
import com.jasmeet.worldnow.viewModels.NewsViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(dataStoreUtil: DataStoreUtil, value: Boolean) {

    val context = LocalContext.current

    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    var themeSwitchState by rememberSaveable { mutableStateOf(value) }
    var notificationSwitchState by rememberSaveable { if (
        notificationPermissionState.status.isGranted) mutableStateOf(true) else mutableStateOf(false)
    }

    val newsViewModel :NewsViewModel = hiltViewModel()

    val token = stringResource(R.string.default_web_client_id)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .requestProfile()
            .build()
    }


    val namePhotoPair = remember {
        mutableStateOf(Pair("", ""))
    }

    var isEditDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val country = rememberSaveable {
        mutableStateOf("")

    }

    var newName by remember {
        mutableStateOf("")
    }


    val width = LocalConfiguration.current.screenWidthDp.dp

    val coroutineScope = rememberCoroutineScope()

    var isConfirmedClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var photoUri: Uri? by rememberSaveable { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        photoUri = uri

    }

    val themeState = if (themeSwitchState) "Dark" else "Light"
    val notificationState = if (notificationSwitchState) "Enabled" else "Disabled"

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }




    LaunchedEffect(
        key1 = true,
        block = {
            namePhotoPair.value= newsViewModel.getProfileImgAndName()
            country.value = newsViewModel.getCountry()
        }
    )
    LaunchedEffect(key1 = isConfirmedClicked, block ={
        namePhotoPair.value= newsViewModel.getProfileImgAndName()
    } )

    val settingsItem = listOf(
        SettingsItem(
            title = "Country",
            value =country.value
        ),
        SettingsItem(
            title = "Language",
            value= "English"
        ),
        SettingsItem(
            title = "Your Interest",
            value= "Edit",
            onclick = {
                AppRouter.navigateTo(Screens.UpdateInterestsScreen)
            }
        ),

        )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile".uppercase(),
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = helventica,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCFF4D2),
                            textAlign = TextAlign.Center,
                        )
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            AppRouter.navigateTo(Screens.HomeScreen)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xffCFF4D2),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xff215273)
                ),
            )
        },
    ) {paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Space(height = 10.dp)
            var isConfirmedClicked by remember { mutableStateOf(false) }



            val painter = if (photoUri != null) {
                Log.d("ProfileScreen", "ProfileScreenLayout: $photoUri")
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(photoUri)
                        .crossfade(true)
                        .size(1500)
                        .transformations(CircleCropTransformation())
                        .build()
                )
            } else {
                Log.d("Firebase", "SettingsScreen: ${namePhotoPair.value.second}")
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(namePhotoPair.value.second)
                        .crossfade(true)
                        .size(1500)
                        .transformations(CircleCropTransformation())
                        .build()
                )
            }

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
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
                    contentScale = ContentScale.FillBounds
                )

                if (!isConfirmedClicked) {
                    if (photoUri == null) {
                        Card(
                            shape = CircleShape,
                            modifier = Modifier
                                .offset(x = (-2).dp, y = 15.dp),
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
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    } else {
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

                        Card(
                            shape = CircleShape,
                            modifier = Modifier
                                .offset(x = (0).dp, y = (10).dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = Color(0x80000000)
                            ),
                            onClick = {
                                coroutineScope.launch {
                                    isConfirmedClicked = true
                                    newsViewModel.updateProfileImg(
                                        photoUri!!,
                                        context.contentResolver
                                    )
                                }

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done, contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }

            Space(15.dp)

            OutlinedTextField(
                value = namePhotoPair.value.first,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                enabled = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = MaterialTheme.colorScheme.background,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.background,
                ),
                label = {
                    Text(text = "Name")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isEditDialogVisible = true

                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription =null )
                    }

                }
            )

            Space(height = 35.dp)

            Text(
                text ="Preferences",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helventica
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )

            Space(height = 10.dp)

            Column(modifier = Modifier
                .padding(start = 20.dp,end= 20.dp, bottom = 15.dp)
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                settingsItem.forEach {
                    Column(  modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = it.title,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = inter
                                )
                            )
                            OutlinedCard(
                                onClick = {
                                    it.onclick.invoke()

                                },
                                modifier = Modifier.bounceClick()
                            ) {
                                Text(
                                    text = it.value,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = inter
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                )
                            }

                        }
                        Space(height = 5.dp)
                        Divider(color = MaterialTheme.colorScheme.primary)
                    }

                }

                Space(height = 5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text ="Notifications : $notificationState", style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = inter
                    ))

                    NotificationSwitch(
                        notificationSwitchState,
                        onCheckedChange = {
                            notificationSwitchState = it
                        },
                    )
                    if (notificationSwitchState){
                        notificationPermissionState.launchPermissionRequest()
                    }

                }
                Space(height = 5.dp)
                Divider(color = MaterialTheme.colorScheme.primary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text ="Theme : $themeState",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = inter
                        )
                    )

                    ThemeSwitch(
                        themeSwitchState,
                        onCheckedChange = {
                            themeSwitchState = it
                            if (!it){
                                notificationPermissionState.launchPermissionRequest()
                            }
                        },
                        dataStoreUtil = dataStoreUtil
                    )

                }
                Space(height = 5.dp)

                Spacer(modifier = Modifier.weight(1f))

                ButtonComponent(
                    onclick = {
                       newsViewModel.logout(googleSignInClient)

//                              val imgUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
//                        val sendNotification = SendNotification(context)
//                        sendNotification.execute("title", "message", imgUrl)
//                        scheduleRepeatingNotification(
//                            context = context,
//                            imageURL = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
//                            title = "Singh is King"
//                        )


                    },
                    text = "Logout",
                    isEnabled = true
                )
            }
        }

        if (isEditDialogVisible){
            Dialog(
                onDismissRequest = {
                    isEditDialogVisible = false
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                )
            ) {
                Box(modifier = Modifier
                    .wrapContentHeight()
                    .width(width * 0.85f)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                    .clip(
                        MaterialTheme.shapes.large
                    )

                ){
                    ConstraintLayout(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 15.dp)) {
                        val (text, textField,closeIcon,confirmButton) = createRefs()

                        IconButton(
                            onClick = {
                                isEditDialogVisible = false
                                newName = ""
                            },
                            modifier = Modifier.constrainAs(closeIcon) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "close", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }

                        Text(
                            text = "Update your UserName",
                            modifier = Modifier.constrainAs(text)
                            {
                                top.linkTo(parent.top, margin = 15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }, style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = helventica,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                            )
                        )
                        OutlinedTextField(
                            shape = MaterialTheme.shapes.small,
                            value = newName,
                            onValueChange = {
                                newName = it
                            },
                            maxLines = 1,
                            label = {
                                Text(text = "Name")
                            },
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .fillMaxWidth()
                                .constrainAs(textField) {
                                    top.linkTo(text.bottom, margin = 15.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (newName.trim().isEmpty()){
                                        showToast(context, "Please enter a name")
                                    }else{
                                        coroutineScope.launch {
                                            isConfirmedClicked = true
                                            newsViewModel.updateName(newName)
                                            isEditDialogVisible = false
                                        }
                                    }


                                }
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                                autoCorrect = false,
                            )
                        )

                        Button(
                            onClick = {
                                if (newName.trim().isEmpty()){
                                    showToast(context, "Please enter a name")
                                }else{
                                    coroutineScope.launch {
                                        isConfirmedClicked = true
                                        newsViewModel.updateName(newName)
                                        isEditDialogVisible = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .bounceClick()
                                .fillMaxWidth()
                                .constrainAs(confirmButton) {
                                    top.linkTo(textField.bottom, margin = 15.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            shape = MaterialTheme.shapes.small,
                        ){
                            Text(
                                text = "Confirm",
                                fontFamily = inter,
                                fontSize = 16.sp,
                                fontWeight = FontWeight(800),
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(2.dp))
                        }

                    }
                }

            }
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.HomeScreen)
    }
}
@Composable
fun ThemeSwitch(
    isCheck: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    dataStoreUtil: DataStoreUtil
) {
    val coroutineScope = rememberCoroutineScope()

    Switch(
        checked = isCheck,
        onCheckedChange = {
            onCheckedChange(it)
            coroutineScope.launch {
                dataStoreUtil.saveTheme(it)
            }
        },
        thumbContent = {
            if (isCheck) {
                Icon(
                    imageVector = Icons.Outlined.DarkMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )

            }
        },
    )
}

data class SettingsItem(
    var title:String ,
    var value :String,
    var onclick :() ->Unit = {}
)

@Composable
fun NotificationSwitch(
    isCheck: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        checked = isCheck,
        onCheckedChange = {
            onCheckedChange(it)
        },

        thumbContent = {
            if (isCheck) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsActive,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.NotificationsOff,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )

            }
        },
    )

}


