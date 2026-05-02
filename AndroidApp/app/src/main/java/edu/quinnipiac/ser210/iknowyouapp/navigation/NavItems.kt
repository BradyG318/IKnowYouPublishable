package com.example.navigationdrawer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(val route: String) {
    HomeScreen("home"),
    GlassesSettingScreen("glassessetting"),
    DataScreen("data"),
    SettingScreen("setting"),
    DetailedDataScreen("info")
}
data class NavItems (
    val title:String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val route:String
)
val listOfNavItems = listOf<NavItems>(
    NavItems(
        title = "Home",
        unselectedIcon= Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = Screens.HomeScreen.route
    ),
    NavItems(
        title = "Glasses Settings",
        unselectedIcon= Icons.Outlined.Build,
        selectedIcon = Icons.Filled.Build,
        route = Screens.GlassesSettingScreen.route
    ),
    NavItems(
        title = "Data",
        unselectedIcon= Icons.Outlined.Face,
        selectedIcon = Icons.Filled.Face,
        route = Screens.DataScreen.route
    ),
    NavItems(
        title = "App Settings",
        unselectedIcon= Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        route = Screens.SettingScreen.route
    )
)