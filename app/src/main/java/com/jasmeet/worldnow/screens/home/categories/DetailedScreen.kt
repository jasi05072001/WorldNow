@file:Suppress("DEPRECATION")

package com.jasmeet.worldnow.screens.home.categories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.Space
import com.jasmeet.worldnow.appComponents.bounceClick
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.utils.removeBrackets
import com.jasmeet.worldnow.utils.removeWhitespaces

@Composable
fun DetailedScreen() {
    DetailedScreenLayout()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedScreenLayout() {

    val context = LocalContext.current
    val articleDetails = AppRouter.detailedArticles
    val height = LocalConfiguration.current.screenHeightDp.dp

    val imgUrl =
        if (articleDetails?.urlToImage.isNullOrEmpty()) "https://demofree.si.com/nope-not-here.jpg" else removeWhitespaces(
            articleDetails!!.urlToImage
        )
    val displayImg = removeBrackets(imgUrl)

    val imageBuilder = ImageRequest.Builder(context)
        .data(displayImg)
        .crossfade(true)
        .build()

    val content = articleDetails!!.content
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}


    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
    val isSystemInDark = isSystemInDarkTheme()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = TextStyle(
                            fontSize = 30.sp,
                            lineHeight = 22.sp,
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
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Menu",
                            tint = Color(0xffCFF4D2),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xff215273)
                ),
                scrollBehavior = scrollBehaviour,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "Add to bookmark",
                            Modifier.size(30.dp)
                        )

                    }
                    IconButton(onClick = {
                        val url = articleDetails.url
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, url)
                            type = "text/plain"
                        }
                        val chooser = Intent.createChooser(shareIntent, "Share Article")
                        shareLauncher.launch(chooser) }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Add to bookmark",
                            Modifier.size(30.dp)

                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .height(height / 4)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AsyncImage(
                        model = imageBuilder, contentDescription = null,
                        modifier = Modifier
                            .height(height / 4)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillBounds,
                    )
                    Text(
                        text = articleDetails.title,
                        modifier = Modifier
                            .background(Color(0x80000000))
                            .fillMaxWidth()
                            .padding(5.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = inter,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        softWrap = true,
                        overflow = TextOverflow.Visible
                    )
                }
            }

            Space(height = 15.dp)

            Text(
                text = articleDetails.description,
                modifier = Modifier.padding(horizontal = 15.dp),
                fontWeight = FontWeight.W300,
                fontSize = 20.sp
            )
            Space(height =15.dp)
            Text(
                text = content.replace(Regex("\\n{2}"), ""),
                modifier = Modifier.padding(horizontal = 15.dp),
                fontWeight = FontWeight.W300,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(15.dp))
            Surface(
                modifier = Modifier
                    .bounceClick()
                    .clip(RoundedCornerShape(7.dp))
                    .padding(bottom = 15.dp, end = 15.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        openTab(
                            url = articleDetails.url,
                            context = context,
                            isDarkMode = isSystemInDark
                        )
                    },
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(7.dp),
            ) {
                Text(
                    text ="Read More",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontFamily = inter,
                    fontWeight = FontWeight.W500
                )

            }

        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.HomeScreen)
    }
}

fun openTab(context: Context,url:String,isDarkMode:Boolean) {
    val package_name = "com.android.chrome"
    val activity = (context as? Activity)

    val builder = CustomTabsIntent.Builder()

    builder.apply {
        setShowTitle(true)
        setInstantAppsEnabled(true)
        setToolbarColor(if (isDarkMode)ContextCompat.getColor(context, R.color.light) else ContextCompat.getColor(context, R.color.dark))
        setNavigationBarColor(if (isDarkMode)ContextCompat.getColor(context, R.color.light) else ContextCompat.getColor(context, R.color.dark))

    }

    val customBuilder = builder.build()
    if (package_name != null) {
        customBuilder.intent.setPackage(package_name)
        customBuilder.launchUrl(context, Uri.parse(url))
    } else {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity?.startActivity(i)
    }

}