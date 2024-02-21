package com.xbot.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@ExperimentalMaterial3Api
@Composable
fun LargeTopAppBarTitleScale(
    modifier: Modifier = Modifier,
    title: @Composable TitleScope.() -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    titleTextStyle: TextStyle = MaterialTheme.typography.displayLarge,
    collapsedTitleTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    LargeTopAppBarContent(
        modifier = modifier,
        title = { TitleScopeImpl(singleLine = false).title() },
        titleOverlay = { TitleScopeImpl(singleLine = false, maxLines = 1).title() },
        titleTextStyle = titleTextStyle,
        titleBottomPadding = LargeTitleBottomPadding,
        collapsedTitle = { TitleScopeImpl(singleLine = true).title() },
        collapsedTitleTextStyle = collapsedTitleTextStyle,
        navigationIcon = navigationIcon,
        actions = actions,
        maxHeight = TopAppBarMaxHeight,
        pinnedHeight = TopAppBarPinnedHeight,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

@ExperimentalMaterial3Api
@Composable
private fun LargeTopAppBarContent(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    titleOverlay: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    titleBottomPadding: Dp,
    collapsedTitle: @Composable () -> Unit,
    collapsedTitleTextStyle: TextStyle,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    maxHeight: Dp,
    pinnedHeight: Dp,
    windowInsets: WindowInsets,
    colors: TopAppBarColors,
    scrollBehavior: TopAppBarScrollBehavior?
) {

    if (maxHeight <= pinnedHeight) {
        throw IllegalArgumentException(
            "A TwoRowsTopAppBar max height should be greater than its pinned height"
        )
    }

    val pinnedHeightPx: Float
    val maxHeightPx: Float
    val titleBottomPaddingPx: Int

    LocalDensity.current.run {
        pinnedHeightPx = pinnedHeight.toPx()
        maxHeightPx = maxHeight.toPx()
        titleBottomPaddingPx = titleBottomPadding.roundToPx()
    }

    val actionsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }

    val titleStyled = @Composable {
        ProvideTextStyle(
            value = titleTextStyle,
            content = title
        )
    }

    val collapsedTitleStyled = @Composable {
        ProvideTextStyle(
            value = titleTextStyle,
            content = collapsedTitle
        )
    }

    DimensionSubcomposeLayout(
        contents = persistentListOf(titleStyled, collapsedTitleStyled)
    ) { (titleSize, collapsedTitleSize) ->

        val additionalHeightPx = titleSize.height - collapsedTitleSize.height
        val scrollMaxDistance = pinnedHeightPx - maxHeightPx - additionalHeightPx
        SideEffect {
            if (scrollBehavior?.state?.heightOffsetLimit != scrollMaxDistance) {
                scrollBehavior?.state?.heightOffsetLimit = scrollMaxDistance
                //Updating current heightOffset after layout height recalculating
                scrollBehavior?.state?.heightOffset =
                    when (scrollBehavior?.state?.heightOffset ?: 0f) {
                        0f -> 0f
                        else -> scrollMaxDistance
                    }
            }
        }

        val transitionFraction = scrollBehavior?.state?.collapsedFraction ?: 0f

        val appBarDragModifier = if (scrollBehavior != null && !scrollBehavior.isPinned) {
            Modifier.draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
                },
                onDragStopped = { velocity ->
                    settleAppBar(
                        scrollBehavior.state,
                        velocity,
                        scrollBehavior.flingAnimationSpec,
                        scrollBehavior.snapAnimationSpec
                    )
                }
            )
        } else {
            Modifier
        }

        Surface(
            modifier = modifier.then(appBarDragModifier),
            color = colors.containerColor
        ) {
            LargeTopAppBarLayout(
                modifier = Modifier
                    .clipToBounds()
                    .windowInsetsPadding(windowInsets),
                heightProvider = {
                    maxHeightPx + additionalHeightPx + (scrollBehavior?.state?.heightOffset ?: 0f)
                },
                pinnedHeightPx = pinnedHeightPx,
                navigationIconContentColor = colors.navigationIconContentColor,
                titleContentColor = colors.titleContentColor,
                actionIconContentColor = colors.actionIconContentColor,
                title = title,
                titleOverlay = titleOverlay,
                titleTextStyle = titleTextStyle,
                collapsedTitle = collapsedTitle,
                collapsedTitleTextStyle = collapsedTitleTextStyle,
                navigationIcon = navigationIcon,
                actions = actionsRow,
                transitionFractionProvider = { transitionFraction },
                titleBottomPadding = titleBottomPaddingPx
            )
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun LargeTopAppBarLayout(
    modifier: Modifier,
    heightProvider: () -> Float,
    pinnedHeightPx: Float,
    navigationIconContentColor: Color,
    titleContentColor: Color,
    actionIconContentColor: Color,
    title: @Composable () -> Unit,
    titleOverlay: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    collapsedTitle: @Composable () -> Unit,
    collapsedTitleTextStyle: TextStyle,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    transitionFractionProvider: () -> Float,
    titleScaleTransitionEasing: Easing = EaseOutQuad,
    titlePositionTransitionVerticalEasing: Easing = EaseInOutSine,
    titlePositionTransitionHorizontalEasing: Easing = LinearEasing,
    textStyleEasing: Easing = FastOutSlowInEasing,
    titleBottomPadding: Int
) {
    val converter = rememberFontScaleConverter()

    val expandedFontSize = converter?.convertSpToDp(titleTextStyle.fontSize.value)
        ?: titleTextStyle.fontSize.value
    val collapsedFontSize = converter?.convertSpToDp(collapsedTitleTextStyle.fontSize.value)
        ?: collapsedTitleTextStyle.fontSize.value
    val expandedLineHeight = converter?.convertSpToDp(titleTextStyle.lineHeight.value)
        ?: titleTextStyle.lineHeight.value
    val collapsedLineHeight = collapsedFontSize * (expandedLineHeight / expandedFontSize)

    val expandedScaleRatio = lerp(
        start = 1f,
        stop = collapsedLineHeight / expandedLineHeight,
        fraction = titleScaleTransitionEasing.transform(transitionFractionProvider())
    )
    val collapsedScaleRatio = lerp(
        start = expandedLineHeight / collapsedLineHeight,
        stop = 1f,
        fraction = titleScaleTransitionEasing.transform(transitionFractionProvider())
    )

    val currentExpandedTextStyle = titleTextStyle.blend(
        start = titleTextStyle,
        stop = collapsedTitleTextStyle,
        fraction = transitionFractionProvider()
    )
    val currentCollapsedTextStyle = collapsedTitleTextStyle.blend(
        start = titleTextStyle,
        stop = collapsedTitleTextStyle,
        fraction = transitionFractionProvider()
    )

    val titleBox = @Composable {
        Box(
            Modifier
                .padding(horizontal = TopAppBarHorizontalPadding)
                .graphicsLayer {
                    alpha = lerp(
                        start = 1f,
                        stop = 0f,
                        fraction = textStyleEasing.transform(transitionFractionProvider())
                    )
                    scaleX = expandedScaleRatio
                    scaleY = expandedScaleRatio
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            ProvideTextStyle(value = currentExpandedTextStyle) {
                CompositionLocalProvider(
                    LocalContentColor provides titleContentColor,
                    content = title
                )
            }
        }
    }

    val titleOverlayBox = @Composable {
        Box(modifier = Modifier
            .padding(horizontal = TopAppBarHorizontalPadding)
            .graphicsLayer {
                scaleX = expandedScaleRatio
                scaleY = expandedScaleRatio
                transformOrigin = TransformOrigin(0f, 0f)
            }
        ) {
            ProvideTextStyle(value = currentExpandedTextStyle) {
                CompositionLocalProvider(
                    LocalContentColor provides titleContentColor,
                    content = titleOverlay
                )
            }
        }
    }

    val collapsedTitleBox = @Composable {
        Box(
            Modifier
                .padding(horizontal = TopAppBarHorizontalPadding)
                .graphicsLayer {
                    alpha = 1f - lerp(
                        start = 0f,
                        stop = 1f,
                        fraction = textStyleEasing.transform(1f - transitionFractionProvider())
                    )
                    scaleX = collapsedScaleRatio
                    scaleY = collapsedScaleRatio
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            ProvideTextStyle(
                value = currentCollapsedTextStyle.copy(
                    lineHeight = TextUnit(collapsedLineHeight, TextUnitType.Sp)
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides titleContentColor,
                    content = collapsedTitle
                )
            }
        }
    }

    val navigationIconBox = @Composable {
        Box(
            Modifier
                .padding(start = TopAppBarHorizontalPadding)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides navigationIconContentColor,
                content = navigationIcon
            )
        }
    }

    val actionIconsBox = @Composable {
        Box(
            Modifier
                .padding(end = TopAppBarHorizontalPadding)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides actionIconContentColor,
                content = actions
            )
        }
    }

    Layout(
        modifier = modifier,
        contents = listOf(titleBox, titleOverlayBox, collapsedTitleBox, navigationIconBox, actionIconsBox)
    ) { (titleMeasurable, titleOverlayMeasurable, collapsedTitleMeasurable, navigationIconMeasurable, actionIconsMeasurable), constraints ->

        val navigationIconPlaceable = navigationIconMeasurable.first()
            .measure(constraints.copy(minWidth = 0))

        val actionIconsPlaceable = actionIconsMeasurable.first()
            .measure(constraints.copy(minWidth = 0))

        val maxTitleWidth = if (constraints.maxWidth == Constraints.Infinity) {
            constraints.maxWidth
        } else {
            (constraints.maxWidth - navigationIconPlaceable.width - actionIconsPlaceable.width)
                .coerceAtLeast(0)
        }

        val titlePlaceable = titleMeasurable.first()
            .measure(constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth))

        val titleOverlayPlaceable = titleOverlayMeasurable.first()
            .measure(constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth))

        val collapsedTitlePlaceable = collapsedTitleMeasurable.first()
            .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))

        val titleBaseline =
            if (titlePlaceable[LastBaseline] != AlignmentLine.Unspecified) {
                titlePlaceable[LastBaseline]
            } else {
                0
            }

        val layoutHeight = heightProvider().roundToInt()

        val expandedX = TopAppBarTitleInset.roundToPx()
        val collapsedX = max(TopAppBarTitleInset.roundToPx(), navigationIconPlaceable.width)

        val expandedY = (heightProvider() - titlePlaceable.height * expandedScaleRatio - max(
            0,
            titleBottomPadding - titlePlaceable.height + titleBaseline
        )).roundToInt()
        val collapsedY =
            ((pinnedHeightPx - (collapsedTitlePlaceable.height) * collapsedScaleRatio) / 2).roundToInt()

        layout(constraints.maxWidth, layoutHeight) {

            navigationIconPlaceable.placeRelative(
                x = 0,
                y = (pinnedHeightPx.roundToInt() - navigationIconPlaceable.height) / 2
            )

            titlePlaceable.placeRelative(
                x = lerp(
                    start = expandedX,
                    stop = collapsedX,
                    fraction = titlePositionTransitionHorizontalEasing.transform(
                        transitionFractionProvider()
                    )
                ),
                y = lerp(
                    start = expandedY,
                    stop = collapsedY,
                    fraction = titlePositionTransitionVerticalEasing.transform(
                        transitionFractionProvider()
                    )
                )
            )
            titleOverlayPlaceable.placeRelative(
                x = lerp(
                    start = expandedX,
                    stop = collapsedX,
                    fraction = titlePositionTransitionHorizontalEasing.transform(
                        transitionFractionProvider()
                    )
                ),
                y = lerp(
                    start = expandedY,
                    stop = collapsedY,
                    fraction = titlePositionTransitionVerticalEasing.transform(
                        transitionFractionProvider()
                    )
                )
            )
            collapsedTitlePlaceable.placeRelative(
                x = lerp(
                    start = expandedX,
                    stop = collapsedX,
                    fraction = titlePositionTransitionHorizontalEasing.transform(
                        transitionFractionProvider()
                    )
                ),
                y = lerp(
                    start = expandedY,
                    stop = collapsedY,
                    fraction = titlePositionTransitionVerticalEasing.transform(
                        transitionFractionProvider()
                    )
                )
            )

            actionIconsPlaceable.placeRelative(
                x = constraints.maxWidth - actionIconsPlaceable.width,
                y = (pinnedHeightPx.roundToInt() - actionIconsPlaceable.height) / 2
            )
        }
    }
}

@ExperimentalMaterial3Api
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {

    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) { state.heightOffset = value }
        }
    }

    return Velocity(0f, remainingVelocity)
}

object TopAppBarDefaults {

    val windowInsets: WindowInsets
        @Composable
        get() = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    @Composable
    fun topAppBarColors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        navigationIconContentColor: Color = MaterialTheme.colorScheme.onSurface,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor: Color = MaterialTheme.colorScheme.onSurface,
    ): TopAppBarColors = TopAppBarColors(
        containerColor,
        navigationIconContentColor,
        titleContentColor,
        actionIconContentColor
    )
}

@Stable
class TopAppBarColors internal constructor(
    internal val containerColor: Color,
    internal val navigationIconContentColor: Color,
    internal val titleContentColor: Color,
    internal val actionIconContentColor: Color,
)

interface TitleScope {

    @Composable
    fun Text(
        text: String
    )
}

internal class TitleScopeImpl(
    private val singleLine: Boolean,
    private val maxLines: Int = Int.MAX_VALUE
) : TitleScope {

    @Composable
    override fun Text(
        text: String
    ) {
        Text(
            text = if (singleLine) text.replace("\n", " ") else text,
            maxLines = if (singleLine) 1 else maxLines,
            overflow = if (singleLine) TextOverflow.Ellipsis else TextOverflow.Clip,
            softWrap = !singleLine
        )
    }
}

private val TopAppBarPinnedHeight = 64.dp
private val TopAppBarMaxHeight = 152.dp

private val LargeTitleBottomPadding = 28.dp
private val TopAppBarHorizontalPadding = 4.dp

private val TopAppBarTitleInset = 16.dp - TopAppBarHorizontalPadding
