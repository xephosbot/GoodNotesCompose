package com.xbot.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SelectableChip(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    label: @Composable () -> Unit,
    labelTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    enabled: Boolean = true,
    leadingIcon: @Composable (SelectableChipScope.() -> Unit)? = null,
    shape: Shape = SelectableChipDefaults.shape,
    colors: SelectableChipColors = SelectableChipDefaults.selectableChipColors(),
    border: SelectableChipBorder = SelectableChipDefaults.selectableChipBorder(),
    interactionSource: MutableInteractionSource? = null
) {
    // TODO: Delete this after Compose 1.7.0
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    Surface(
        enabled = enabled,
        selected = selected,
        modifier = modifier.semantics { role = Role.Button },
        onClick = onClick,
        shape = shape,
        color = colors.containerColor(enabled = enabled, selected = selected).value,
        border = border.borderStroke(enabled = enabled, selected = selected).value,
        interactionSource = interactionSource
    ) {
        ChipContent(
            label = label,
            labelTextStyle = labelTextStyle,
            labelColor = colors.labelColor(enabled = enabled, selected = selected).value,
            leadingIcon = if (leadingIcon != null) {
                {
                    AnimatedVisibility(
                        visible = selected,
                        enter = enterTransition(),
                        exit = exitTransition()
                    ) {
                        val scope = remember(transition.isRunning, selected) {
                            SelectableChipScopeImpl(transition.isRunning && selected)
                        }
                        scope.leadingIcon()
                    }
                }
            } else null,
            leadingIconColor = colors.leadingIconContentColor(
                enabled = enabled,
                selected = selected
            ).value,
            minHeight = ContainerHeight,
            paddingValues = ChipPadding
        )
    }
}

@Composable
private fun ChipContent(
    label: @Composable () -> Unit,
    labelTextStyle: TextStyle,
    labelColor: Color,
    leadingIcon: @Composable (() -> Unit)?,
    leadingIconColor: Color,
    minHeight: Dp,
    paddingValues: PaddingValues
) {
    CompositionLocalProvider(
        LocalContentColor provides labelColor,
        LocalTextStyle provides labelTextStyle
    ) {
        Row(
            Modifier
                .defaultMinSize(minHeight = minHeight)
                .padding(paddingValues),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                CompositionLocalProvider(
                    LocalContentColor provides leadingIconColor, content = leadingIcon
                )
            }
            Spacer(Modifier.width(HorizontalElementsPadding))
            label()
            Spacer(Modifier.width(HorizontalElementsPadding))
        }
    }
}

@Stable
private fun enterTransition() = fadeIn() + expandIn(
    expandFrom = Alignment.Center,
    clip = false
) + scaleIn(initialScale = 0.4f)

@Stable
private fun exitTransition() = scaleOut(targetScale = 0.8f) + shrinkOut(
    shrinkTowards = Alignment.Center,
    clip = false
) + fadeOut()

@Composable
fun SelectableChipScope.SelectableChipBadge(
    modifier: Modifier = Modifier,
    text: String,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    style: TextStyle = MaterialTheme.typography.titleSmall,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val currentText by remember(shouldUpdateLabel) { mutableStateOf(text) }

    Surface(
        modifier = modifier
            .defaultMinSize(
                minWidth = IconSize,
                minHeight = IconSize
            ),
        color = color,
        contentColor = contentColor,
        shape = shape
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentText,
                style = style
            )
        }
    }
}

interface SelectableChipScope {

    val shouldUpdateLabel: Boolean
}

private class SelectableChipScopeImpl(
    override val shouldUpdateLabel: Boolean
) : SelectableChipScope

@Immutable
class SelectableChipColors internal constructor(
    private val containerColor: Color,
    private val labelColor: Color,
    private val leadingIconColor: Color,
    private val disabledContainerColor: Color,
    private val disabledLabelColor: Color,
    private val disabledLeadingIconColor: Color,
    private val selectedContainerColor: Color,
    private val disabledSelectedContainerColor: Color,
    private val selectedLabelColor: Color,
    private val selectedLeadingIconColor: Color,
) {
    @Composable
    internal fun containerColor(enabled: Boolean, selected: Boolean): State<Color> {
        val target = when {
            !enabled -> if (selected) disabledSelectedContainerColor else disabledContainerColor
            !selected -> containerColor
            else -> selectedContainerColor
        }
        return rememberUpdatedState(target)
    }

    @Composable
    internal fun labelColor(enabled: Boolean, selected: Boolean): State<Color> {
        val target = when {
            !enabled -> disabledLabelColor
            !selected -> labelColor
            else -> selectedLabelColor
        }
        return rememberUpdatedState(target)
    }

    @Composable
    internal fun leadingIconContentColor(enabled: Boolean, selected: Boolean): State<Color> {
        val target = when {
            !enabled -> disabledLeadingIconColor
            !selected -> leadingIconColor
            else -> selectedLeadingIconColor
        }
        return rememberUpdatedState(target)
    }
}

@Immutable
class SelectableChipBorder internal constructor(
    private val borderColor: Color,
    private val selectedBorderColor: Color,
    private val disabledBorderColor: Color,
    private val disabledSelectedBorderColor: Color,
    private val borderWidth: Dp,
    private val selectedBorderWidth: Dp
) {

    @Composable
    internal fun borderStroke(enabled: Boolean, selected: Boolean): State<BorderStroke?> {
        val color = if (enabled) {
            if (selected) selectedBorderColor else borderColor
        } else {
            if (selected) disabledSelectedBorderColor else disabledBorderColor
        }
        return rememberUpdatedState(
            BorderStroke(if (selected) selectedBorderWidth else borderWidth, color)
        )
    }
}

object SelectableChipDefaults {

    @Composable
    fun selectableChipColors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        labelColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        iconColor: Color = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledLabelColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledLeadingIconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        disabledSelectedContainerColor: Color = disabledContainerColor,
        selectedLabelColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedLeadingIconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
    ): SelectableChipColors = SelectableChipColors(
        containerColor = containerColor,
        labelColor = labelColor,
        leadingIconColor = iconColor,
        disabledContainerColor = disabledContainerColor,
        disabledLabelColor = disabledLabelColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        selectedContainerColor = selectedContainerColor,
        disabledSelectedContainerColor = disabledSelectedContainerColor,
        selectedLabelColor = selectedLabelColor,
        selectedLeadingIconColor = selectedLeadingIconColor
    )

    @Composable
    fun selectableChipBorder(
        borderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        selectedBorderColor: Color = Color.Transparent,
        disabledBorderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledSelectedBorderColor: Color = Color.Transparent,
        borderWidth: Dp = 1.dp,
        selectedBorderWidth: Dp = 1.dp
    ): SelectableChipBorder = SelectableChipBorder(
        borderColor = borderColor,
        selectedBorderColor = selectedBorderColor,
        disabledBorderColor = disabledBorderColor,
        disabledSelectedBorderColor = disabledSelectedBorderColor,
        borderWidth = borderWidth,
        selectedBorderWidth = selectedBorderWidth
    )

    val shape: Shape @Composable get() = MaterialTheme.shapes.extraSmall
}

private val ContainerHeight = 40.0.dp
private val HorizontalElementsPadding = 8.dp
private val ChipPadding = PaddingValues(horizontal = HorizontalElementsPadding)
private val IconSize = 24.dp
