package com.example.navigationdrawer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.navigationdrawer.screen.HomeScreen
import com.example.navigationdrawer.screen.SettingScreen
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.navigationdrawer.screen.DataScreen
import com.example.navigationdrawer.screen.GlassesSettingScreen
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel
import edu.quinnipiac.ser210.iknowyouapp.connection.BluetoothHelper
import edu.quinnipiac.ser210.iknowyouapp.screen.DetainedDataScreen
import edu.quinnipiac.ser210.iknowyouapp.ui.theme.AppTheme

@Composable
fun AppNavigation(snackbarHostState:SnackbarHostState,navController: NavHostController,iKnowYouViewModel: IKnowYouViewModel, bluetoothHelper: BluetoothHelper) {
    NavHost(navController = navController,startDestination = Screens.HomeScreen.route) {
        Screens.values().forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    Screens.HomeScreen -> HomeScreen(iKnowYouViewModel,snackbarHostState,bluetoothHelper)
                    Screens.GlassesSettingScreen -> GlassesSettingScreen(iKnowYouViewModel,bluetoothHelper,snackbarHostState)
                    Screens.DataScreen -> DataScreen(iKnowYouViewModel,navController)
                    Screens.SettingScreen -> SettingScreen(iKnowYouViewModel)
                    Screens.DetailedDataScreen -> DetainedDataScreen(iKnowYouViewModel,navController,"test")
                }
            }
            composable (Screens.DetailedDataScreen.name + "/{id}",
                arguments = listOf(navArgument(name = "id") { type = NavType.StringType })){ backScreenEntry ->
                    val id = backScreenEntry.arguments?.getString("id") ?: ""
                    DetainedDataScreen(iKnowYouViewModel,navController,id.toString())
            }
        }
    }
}

