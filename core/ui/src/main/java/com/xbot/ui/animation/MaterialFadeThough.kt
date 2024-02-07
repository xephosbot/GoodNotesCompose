package com.xbot.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

@ExperimentalAnimationApi
fun materialFadeIn(
    animationSpec: FiniteAnimationSpec<Float> = tween(
        durationMillis = MaterialMotionDefaults.motionDurationLong2,
        easing = FastOutExtraSlowInEasing
    ),
    initialAlpha: Float = 0f,
    initialScale: Float = 0.4f
): EnterTransition = fadeIn(
    animationSpec = animationSpec,
    initialAlpha = initialAlpha
) + scaleIn(
    animationSpec = animationSpec,
    initialScale = initialScale
)

@ExperimentalAnimationApi
fun materialFadeOut(
    animationSpec: FiniteAnimationSpec<Float> = tween(
        durationMillis = MaterialMotionDefaults.motionDurationShort2,
        easing = FastOutLinearInEasing
    ),
    targetAlpha: Float = 0f,
    targetScale: Float = 0.8f
): ExitTransition = fadeOut(
    animationSpec = animationSpec,
    targetAlpha = targetAlpha
) + scaleOut(
    animationSpec = animationSpec,
    targetScale = targetScale
)