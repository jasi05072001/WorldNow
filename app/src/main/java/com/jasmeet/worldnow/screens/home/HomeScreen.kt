@file:Suppress("DEPRECATION")

package com.jasmeet.worldnow.screens.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.data.NavigationItem
import com.jasmeet.worldnow.data.news.Article
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.screens.home.categories.CategoriesView
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.utils.getProfileImgAndName
import com.jasmeet.worldnow.utils.getSelectedInterests
import com.jasmeet.worldnow.viewModels.NewsViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(){
    HomeScreenLayout()

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreenLayout() {
    val context = LocalContext.current

    val token = stringResource(R.string.default_web_client_id)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .requestProfile()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }
    val interests = rememberSaveable { mutableStateOf(listOf<String>()) }
    val rememberedCategoriesNews = rememberSaveable {
        mutableStateOf(emptyList<Article>())
    }
    val selectedButton = remember { mutableIntStateOf(0) }


    val newsViewModel = NewsViewModel()
    val currentDate = java.time.LocalDate.now().minusDays(5).toString()
    val rememberedNews = rememberSaveable {
        mutableStateOf(emptyList<Article>())
    }



    val pagerState = rememberPagerState(pageCount = {rememberedNews.value.size})
    val loading = rememberSaveable { mutableStateOf(true) }

    val namePhotoPair = remember {
        mutableStateOf(Pair("", ""))
    }

    val isDark = isSystemInDarkTheme()

    val scope = rememberCoroutineScope()

    val selectedIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val drawerGradient = if (isSystemInDarkTheme()) Brush.linearGradient(
        colors = listOf(
            Color(0xFF215273),
            Color(0xFF359D9E)
        )
    ) else Brush.linearGradient(
        colors = listOf(
            Color(0xFF55C595),
            Color(0xFF215273)
        )
    )

    val isHomeScreen = rememberSaveable {
        mutableStateOf(true)
    }
    val isCategoriesScreen = rememberSaveable {
        mutableStateOf(false)
    }
    val isSavedScreen = rememberSaveable {
        mutableStateOf(false)
    }
    val isSettingsScreen = rememberSaveable {
        mutableStateOf(false)
    }
    val newsSearched = remember {
        mutableStateOf("")
    }

    val items = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_home_selected),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.ic_home_unselected),
            onItemClick = {
                isHomeScreen.value = true
                isCategoriesScreen.value = false
                isSavedScreen.value = false
                isSettingsScreen.value = false
            }
        ),
        NavigationItem(
            title = "Categories",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_categories_selected),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.ic_categories_unselected),
            onItemClick = {
                isHomeScreen.value = false
                isCategoriesScreen.value = true
                isSavedScreen.value = false
                isSettingsScreen.value = false
            }
        ),
        NavigationItem(
            title = "Saved",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_bookmark_selected),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.ic_bookmark_unselected),
            onItemClick = {
                isHomeScreen.value = false
                isCategoriesScreen.value = false
                isSavedScreen.value = true
                isSettingsScreen.value = false
            }

        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_settings_selected),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.ic_settings_unselected),
            onItemClick = {
                isHomeScreen.value = false
                isCategoriesScreen.value = false
                isSavedScreen.value = false
                isSettingsScreen.value = true
            }
        ),

        )


    LaunchedEffect(
        key1 = isHomeScreen.value ,
        block = {
            loading.value = true
            namePhotoPair.value= getProfileImgAndName()
            isHomeScreen.value = true
            if (isHomeScreen.value){
                newsViewModel.getNews(
                    "trending",
                    1,
                    successCallBack = {
                        rememberedNews.value = it!!.articles
                        loading.value = false
                    },
                    failureCallback = {
                        loading.value = false
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                    },
                    from = currentDate,
                    pagSize = 15

                )
            }
        }
    )
    LaunchedEffect(
        key1 = isCategoriesScreen.value,
        block = {
            loading.value = true
            interests.value = getSelectedInterests()
            namePhotoPair.value= getProfileImgAndName()
            newsViewModel.getNews(
                interests.value[0],
                1,
                successCallBack = {
                    val newsFromApi = it!!.articles
                    rememberedCategoriesNews.value = newsFromApi
                    loading.value = false
                    Log.d("NewsButton", "HomeScreenLayout: $newsFromApi")

                },
                failureCallback = {
                    loading.value = false
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                },
                from = currentDate,
            )
        }

    )


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                drawerContainerColor = Color.Transparent,
                modifier = Modifier.background(
                    drawerGradient,
                    RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                )
            ) {
                Surface(modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                    color = Color.Transparent) {

                }
                items.forEachIndexed{index,item->
                    NavigationDrawerItem(
                        modifier = Modifier
                            .padding(
                                vertical = 7.dp + NavigationDrawerItemDefaults.ItemPadding.calculateTopPadding(),
                                horizontal = 20.dp
                            )
                            .background(
                                if (index == selectedIndex.intValue) Color(0xFF215273) else Color(
                                    0xB355C595
                                ),
                                RoundedCornerShape(10.dp)

                            ),
                        label = {
                            Text(
                                text = item.title,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = inter,
                                    fontWeight = FontWeight(400),

                                    )

                            )
                        },
                        selected = index == selectedIndex.intValue ,
                        onClick = {
                            selectedIndex.intValue = index
                            scope.launch {
                                drawerState.close()
                                item.onItemClick()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedIndex.intValue) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                modifier = Modifier.padding(start = 2.dp)

                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor= Color.Transparent,
                            unselectedContainerColor= Color.Transparent,
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Black,
                            unselectedTextColor = Color.Black,
                            selectedTextColor = Color.White,

                            ),
                        shape = RoundedCornerShape(10.dp)

                    )

                }

            }
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name).uppercase(),
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
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_menu),
                                contentDescription = "Menu",
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
        ){paddingValues ->
            if (isHomeScreen.value){
                HomeScreenMainView(
                    loading = loading,
                    paddingValues= paddingValues,
                    pagerState = pagerState,
                    rememberedNews = rememberedNews,
                    context = context,
                    isDark = isDark,
                )
            }
            if (isCategoriesScreen.value){

                CategoriesView(
                    paddingValues,
                    loading,
                    interests,
                    selectedButton,
                    newsViewModel,
                    rememberedCategoriesNews,
                    context,
                    currentDate,
                    newsSearched
                )


            }

        }
    }

}



fun openTab(context: Context, url:String, isDarkMode:Boolean) {
    val packageName = "com.android.chrome"
    val activity = (context as? Activity)

    val builder = CustomTabsIntent.Builder()

    builder.apply {
        setShowTitle(true)
        setInstantAppsEnabled(true)
        setToolbarColor(if (isDarkMode) ContextCompat.getColor(context, R.color.light) else ContextCompat.getColor(context, R.color.dark))
        setNavigationBarColor(if (isDarkMode) ContextCompat.getColor(context, R.color.light) else ContextCompat.getColor(context, R.color.dark))

    }

    val customBuilder = builder.build()
    if (packageName != null) {
        customBuilder.intent.setPackage(packageName)
        customBuilder.launchUrl(context, Uri.parse(url))
    } else {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity?.startActivity(i)
    }

}
