package com.jasmeet.worldnow.screens.settingAccount.selectCountry

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.SearchFieldComponent
import com.jasmeet.worldnow.appComponents.Space
import com.jasmeet.worldnow.data.countriesList
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.ui.theme.inter

@Composable
fun CountrySelectionScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        CountrySelectionLayout()

    }
}

@Composable
private fun CountrySelectionLayout() {

    val country = remember {
        mutableStateOf("")
    }

    val isCountrySelected = remember { mutableStateOf(false) }

    val searchedText = remember {
        mutableStateOf("")
    }

    val filteredCountries = countriesList.filter {
        it.contains(searchedText.value, ignoreCase = true)
    }

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
        Space(height = 20.dp)

        Text(
            text = "Select Your Country",
            fontSize = 22.sp,
            fontFamily = inter,
            fontWeight = FontWeight(800),
            color = MaterialTheme.colorScheme.primary
        )

        Space(height = 16.dp)

        SearchFieldComponent(
            value = searchedText.value,
            labelValue ="Enter your Country Name",
            onValueChange ={
                searchedText.value = it
            },
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp)
                .weight(0.75f)
        ){
            items(filteredCountries.size){index ->
                CountrySelectionView(
                    selectedCountry = filteredCountries[index],
                    onCountrySelected = {
                            selectedCountry ->
                        country.value = selectedCountry
                        isCountrySelected.value = true
                    },
                    currentCountry = country.value
                )
                Divider()
            }
        }

        ButtonComponent(
            onclick = {
                AppRouter.navigateTo(Screens.SelectingIntrestScreen)
            },
            text = "Continue",
            isEnabled = isCountrySelected.value
        )
        Space(height = 15.dp)


    }
}

@Composable
private fun CountrySelectionView(
    selectedCountry:String,
    onCountrySelected:(String)->Unit,
    currentCountry :String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                selection(selectedCountry, currentCountry, onCountrySelected)
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = selectedCountry,
            fontSize = 18.sp,
            fontFamily = inter,
            fontWeight = FontWeight(800),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
        )
        RadioButton(
            selected = (selectedCountry == currentCountry),
            onClick ={
                selection(selectedCountry, currentCountry, onCountrySelected)
            },
            modifier = Modifier.offset(x = (-17).dp)
        )

    }

}

private fun selection(
    selectedCountry: String,
    currentCountry: String,
    onCountrySelected: (String) -> Unit
) {
    if (selectedCountry != currentCountry) {
        onCountrySelected(selectedCountry)
    }
}

@Preview(device = Devices.PIXEL_4_XL, uiMode  = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CountrySelectionPreview() {
    CountrySelectionScreen()
}