package com.xbot.goodnotes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberGoodNotesAppState(
    navController: NavHostController = rememberNavController()
): GoodNotesAppState {
    return remember(navController) {
        GoodNotesAppState(
            navController = navController
        )
    }
}

@Stable
class GoodNotesAppState(
    val navController: NavHostController
)
