package com.xbot.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.ui.theme.adjustColorAtElevation

@ExperimentalFoundationApi
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    colors: NoteCardColors = NoteCardDefaults.noteCardColors(),
    border: NoteCardBorder = NoteCardDefaults.noteCardBorder(),
    shape: Shape = NoteCardDefaults.shape,
    headlineContent: @Composable () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    bodyContent: @Composable () -> Unit,
    onClick: (Boolean) -> Unit = {},
    onLongClick: (Boolean) -> Unit = {}
) {
    NoteCardContent(
        modifier = modifier,
        selected = selected,
        colors = colors,
        border = border,
        shape = shape,
        onClick = onClick,
        onLongClick = onLongClick,
        headlineContent = headlineContent,
        trailingContent = trailingContent,
        supportingContent = supportingContent,
        bodyContent = bodyContent
    )
}

@ExperimentalFoundationApi
@Composable
private fun NoteCardContent(
    modifier: Modifier = Modifier,
    selected: Boolean,
    colors: NoteCardColors,
    border: NoteCardBorder,
    shape: Shape,
    onClick: (Boolean) -> Unit,
    onLongClick: (Boolean) -> Unit,
    headlineContent: @Composable () -> Unit,
    trailingContent: @Composable (() -> Unit)?,
    supportingContent: @Composable (() -> Unit)?,
    bodyContent: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .combinedClickable(
                onClick = { onClick(selected) },
                onLongClick = { onLongClick(selected) }
            ),
        color = colors.containerColor(selected).value,
        contentColor = colors.contentColor().value,
        shape = shape,
        border = border.borderStroke(selected).value
    ) {
        NoteCardLayout(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = NoteCardDefaults.MinWidth,
                    minHeight = NoteCardDefaults.MinHeight
                )
                .padding(vertical = NoteCardDefaults.NoteCardVerticalPadding),
            headlineContent = headlineContent,
            trailingContent = {
                if (trailingContent != null) trailingContent()
            },
            supportingContent = {
                if (supportingContent != null) supportingContent()
            },
            bodyContent = bodyContent
        )
    }
}

@Composable
fun NoteCardLayout(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
    bodyContent: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.absolutePadding(
                left = NoteCardDefaults.NoteCardHorizontalPadding,
                right = NoteCardDefaults.NoteCardEndPadding
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1.0f)) {
                    headlineContent()
                }
                Box(modifier = Modifier.align(Alignment.Top)) {
                    trailingContent()
                }
            }
            supportingContent()
        }
        Box(modifier = Modifier.padding(horizontal = NoteCardDefaults.NoteCardHorizontalPadding)) {
            bodyContent()
        }
    }
}

object NoteCardDefaults {

    val NoteCardHorizontalPadding = 16.dp
    val NoteCardVerticalPadding = 32.dp
    val NoteCardEndPadding = 12.dp

    val SelectedElevation = 8.dp

    val MinWidth = 120.dp

    val MinHeight = 100.dp

    val shape: Shape @Composable get() = MaterialTheme.shapes.extraLarge

    @Composable
    fun noteCardColors(
        containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor: Color = MaterialTheme.colorScheme.onSurface
    ): NoteCardColors = NoteCardColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun noteCardBorder(
        borderColor: Color = MaterialTheme.colorScheme.primary,
        selectedBorderColor: Color = Color.Transparent,
        borderWidth: Dp = 2.dp,
        selectedBorderWidth: Dp = 2.dp
    ): NoteCardBorder = NoteCardBorder(
        borderColor = borderColor,
        selectedBorderColor = selectedBorderColor,
        borderWidth = borderWidth,
        selectedBorderWidth = selectedBorderWidth
    )
}

@Immutable
class NoteCardColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color
) {

    @Composable
    internal fun containerColor(selected: Boolean): State<Color> {
        return rememberUpdatedState(
            if (selected) {
                MaterialTheme.colorScheme.adjustColorAtElevation(
                    containerColor,
                    NoteCardDefaults.SelectedElevation
                )
            } else containerColor
        )
    }

    @Composable
    internal fun contentColor(): State<Color> {
        return rememberUpdatedState(contentColor)
    }
}

@Immutable
class NoteCardBorder internal constructor(
    private val borderColor: Color,
    private val selectedBorderColor: Color,
    private val borderWidth: Dp,
    private val selectedBorderWidth: Dp
) {
    @Composable
    internal fun borderStroke(selected: Boolean): State<BorderStroke?> {
        val color = if (selected) borderColor else selectedBorderColor
        return rememberUpdatedState(
            BorderStroke(if (selected) selectedBorderWidth else borderWidth, color)
        )
    }
}
