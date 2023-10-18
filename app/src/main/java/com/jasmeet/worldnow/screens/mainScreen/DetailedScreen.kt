package com.jasmeet.worldnow.screens.mainScreen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.Space
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

    val imgUrl = if (articleDetails?.urlToImage.isNullOrEmpty()) "https://demofree.si.com/nope-not-here.jpg" else removeWhitespaces(
        articleDetails!!.urlToImage)
    val displayImg = removeBrackets(imgUrl)

    val imageBuilder = ImageRequest.Builder(context)
        .data(displayImg)
        .crossfade(true)
        .build()

    val content =articleDetails!!.content

//    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
//            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
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
                            AppRouter.navigateTo(Screens.MainScreen1a)
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
//                scrollBehavior = scrollBehaviour
            )
        }
    ) {paddingValues ->
        Column (
            Modifier
                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
        ){
            ElevatedCard(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .height(height / 4)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    AsyncImage(model = imageBuilder, contentDescription =null,
                        modifier = Modifier
                            .height(height / 4)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillBounds,
                    )
                    Text(
                        text = articleDetails!!.title,
                        modifier = Modifier
                            .background(Color(0x80000000))
                            .fillMaxWidth()
                            .padding(5.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = inter,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Space(height = 15.dp)

            Text(text =articleDetails.description, modifier = Modifier.padding(horizontal = 15.dp))
            Text(text =content.replace(Regex("\\n{2}"),""), modifier = Modifier.padding(horizontal = 15.dp))
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "Add to bookmark",
                            Modifier.size(35.dp)
                        )

                    }

                    Row(modifier = Modifier
                        .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xff215273),
                                Color(0xff359D9E)
                            )
                        ),
                        shape = RoundedCornerShape(7.dp)
                    ).clip(RoundedCornerShape(7.dp))
                        .clickable {  },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        Row (
                            Modifier.padding(horizontal = 7. dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text ="Share", fontSize =20.sp, color = Color(0xffCFF4D2))
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.share), contentDescription =null,   Modifier.size(22.dp), tint = Color(0xffCFF4D2) )

                        }
                    }


                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 75.dp)
                        .align(Alignment.Bottom)
                ) {
                    Text(text ="Read more")

                }
            }

        }

    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.MainScreen1a)
    }
}
