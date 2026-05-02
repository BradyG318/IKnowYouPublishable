package edu.quinnipiac.ser210.iknowyouapp.navigation


enum class AppScreens {
    DetailedDataScreen,
    DataScreen,
    HomeScreen;
    companion object {
        fun fromRoute (route: String?): AppScreens
            = when(route?.substringBefore("/"))
            {
                DetailedDataScreen.name -> DetailedDataScreen
                DataScreen.name -> DataScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }

    }

}


