package com.jasmeet.worldnow.data

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon:ImageVector,
    val onItemClick : () -> Unit = {}
)
