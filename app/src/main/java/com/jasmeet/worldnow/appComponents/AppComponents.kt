package com.jasmeet.worldnow.appComponents

import android.graphics.fonts.FontFamily
import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.ui.theme.inter


@Composable
fun TextFieldComponent(
    value: String,
    labelValue: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,


    ) {
    ElevatedCard (
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .heightIn(42.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ){
        TextField(
            value = value,
            onValueChange = {
                onValueChange.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor =  Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = labelValue,
                    fontFamily = inter,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                )
            },
            textStyle = TextStyle(
                fontFamily = inter,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                color = Color.Black,

                ),
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                autoCorrect = false,
            ),
            maxLines = 1,
            singleLine = true,
        )
    }
}

@Composable
fun PasswordFieldComponent(
    value: String,
    labelValue: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Password,

    ) {


    val passwordVisible = remember {
        mutableStateOf(false)
    }
    ElevatedCard (
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .heightIn(42.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ){
        TextField(
            value = value,
            onValueChange = {
                onValueChange.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            colors =  TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor =  Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = labelValue,
                    fontFamily = inter,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                )
            },
            textStyle = TextStyle(
                fontFamily = inter,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                color = Color.Black,

                ),
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions(
                keyboardType =keyboardType,
                imeAction = imeAction,
                autoCorrect = false,
            ),
            maxLines = 1,
            singleLine = true,
            trailingIcon = {

                val iconImage =
                    if (passwordVisible.value)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                val description = if (passwordVisible.value)
                    "Hide Password"
                else
                    "Show Password"

                IconButton(
                    onClick ={
                        passwordVisible.value = !passwordVisible.value}
                ) {
                    Icon(
                        imageVector = iconImage,
                        contentDescription = description,
                        tint = Color.Black
                    )
                }
            },
            visualTransformation =
            if (passwordVisible.value)
                VisualTransformation.None
            else
                PasswordVisualTransformation()
        )
    }
}


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    onclick : () -> Unit,
    text: String,
    isEnabled : Boolean = false,

    ) {
    Button(
        onClick = {
            onclick.invoke()
        },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .then(
                if (isEnabled)
                    Modifier.bounceClick()
                else
                    Modifier
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = if (isSystemInDarkTheme()) Color.LightGray.copy(alpha = 0.5f) else Color.LightGray,
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(10.dp),

        ) {
        Text(
            text = text,
            fontFamily = inter,
            fontSize = 20.sp,
            fontWeight = FontWeight(600),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(4.dp)

        )
    }
}

@Composable
fun EmailText(modifier: Modifier = Modifier) {
    val emailText =  buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = inter,
                fontSize = 14.sp
            )
        ) {
            append("Email")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Red,
                fontFamily = inter,
                letterSpacing = 1.sp,
            )
        ) {
            append(" *")
        }
    }

    Text(
        text = emailText,
        fontFamily = inter,
        fontSize = 16.sp,
        fontWeight = FontWeight(600),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )

}

@Composable
fun PasswordText(modifier: Modifier = Modifier) {



    val passwordText =  buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = inter,
                fontSize = 14.sp
            )
        ) {
            append("Password")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Red,
                fontFamily = inter,
                letterSpacing = 1.sp,
            )
        ) {
            append(" *")
        }
    }

    Text(
        text = passwordText,
        fontFamily = inter,
        fontSize = 16.sp,
        fontWeight = FontWeight(600),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )

}

@Composable
fun ClickableTextComponent(
    initialText: String,
    clickableText: String,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier.wrapContentSize()) {
        Text(
            text = initialText,
            fontFamily = inter,
            fontSize = 14.sp,
            fontWeight = FontWeight(600),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(2.dp)
        )
        Text(
            text = clickableText,
            fontFamily = inter,
            fontSize = 14.sp,
            fontWeight = FontWeight(600),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .bounceClick()
                .padding(2.dp)
                .clickable {
                    onClick()

                }

        )
    }
}


@Composable
fun LoaderComponent() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.loader)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = false

    )
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(MaterialTheme.shapes.medium.topStart)
                ),
            contentAlignment = Alignment.Center
        ) {
            @Suppress("DEPRECATION")
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds

            )
        }

    }
}

@Composable
fun CircleImageView(
    imgRes :Int,
    contentDescription: String? = null,
    size: DpSize = DpSize(100.dp, 100.dp)

) {
    Image(
        painter = painterResource(id = imgRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .clip(CircleShape)
            .padding(top = 15.dp, bottom = 10.dp)
            .size(size)
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

}


@Composable
fun SuccessLottie() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.success2)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = false

    )

    @Suppress("DEPRECATION")
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.fillMaxSize(0.5f),
        contentScale = ContentScale.Fit


    )
}

@Composable
fun Space(height : Dp) {
    Spacer(modifier = Modifier.height(height))
}


@Composable
fun SearchFieldComponent(
    value: String,
    labelValue: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Search,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,

    ) {
    ElevatedCard (
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .heightIn(42.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ){
        TextField(
            value = value,
            onValueChange = {
                onValueChange.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor =  Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = labelValue,
                    fontFamily = inter,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0x80000000),
                    textAlign = TextAlign.Center,
                )
            },
            textStyle = TextStyle(
                fontFamily = inter,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                color = Color.Black,

                ),
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                autoCorrect = false,
            ),
            maxLines = 1,
            singleLine = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search) ,
                    contentDescription = "Search",
                    tint = Color(0xff215273)
                )
            }
        )
    }
}

//@Composable
//fun AutoResizeText(
//    text: String,
//    fontSizeRange: FontSizeRange,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily? = null,
//    letterSpacing: TextUnit = TextUnit.Unspecified,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    style: TextStyle = LocalTextStyle.current,
//) {
//    var fontSizeValue by remember { mutableStateOf(fontSizeRange.max.value) }
//    var readyToDraw by remember { mutableStateOf(false) }
//
//    Text(
//        text = text,
//        color = color,
//        maxLines = maxLines,
//        fontStyle = fontStyle,
//        fontWeight = fontWeight,
//        fontFamily = fontFamily,
//        letterSpacing = letterSpacing,
//        textDecoration = textDecoration,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        overflow = overflow,
//        softWrap = softWrap,
//        style = style,
//        fontSize = fontSizeValue.sp,
//        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() }
//    )
//}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
) {
    init {
        require(min < max) { "min should be less than max, $this" }
        require(step.value > 0) { "step should be greater than 0, $this" }
    }

    companion object {
        private val DEFAULT_TEXT_STEP = 1.sp
    }
}

