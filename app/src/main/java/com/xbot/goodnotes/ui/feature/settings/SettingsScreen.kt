package com.xbot.goodnotes.ui.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SettingsScreenContent() {
    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge
        )
    }
    Text("This is a cool bottom sheet!")
    Text("This is a cool bottom sheet!")
}