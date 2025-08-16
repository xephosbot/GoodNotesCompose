package com.xbot.goodnotes.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.sharedBoundsRevealWithShapeMorph(
    sharedContentState: SharedTransitionScope.SharedContentState,
    sharedTransitionScope: SharedTransitionScope = LocalNavSharedElementScope.current,
    animatedVisibilityScope: AnimatedVisibilityScope = LocalNavAnimatedContentScope.current,
    resizeMode: ResizeMode = ResizeMode.RemeasureToBounds,
    restingShapeCornerRadius: Dp = 0.dp,
    targetShapeCornerRadius: Dp = 0.dp,
    renderInOverlayDuringTransition: Boolean = true,
    keepChildrenSizePlacement: Boolean = true,
): Modifier {
    with(sharedTransitionScope) {
        val animatedProgress = animatedVisibilityScope.transition.animateDp {
            when (it) {
                EnterExitState.PreEnter -> targetShapeCornerRadius
                EnterExitState.Visible -> restingShapeCornerRadius
                EnterExitState.PostExit -> targetShapeCornerRadius
            }
        }

        val clipShape = RoundedCornerShape(animatedProgress.value)
        val modifier = if (keepChildrenSizePlacement) {
            Modifier
                .skipToLookaheadSize()
                .skipToLookaheadPosition()
        } else {
            Modifier
        }

        return this@sharedBoundsRevealWithShapeMorph
            .sharedBounds(
                sharedContentState = sharedContentState,
                animatedVisibilityScope = animatedVisibilityScope,
                resizeMode = resizeMode,
                clipInOverlayDuringTransition = OverlayClip(clipShape),
                renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            )
            .then(modifier)
    }
}
