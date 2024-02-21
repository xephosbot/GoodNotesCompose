package com.xbot.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = CircleShape,
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = CircleShape,
    extraLarge = RoundedCornerShape(48.dp)
)