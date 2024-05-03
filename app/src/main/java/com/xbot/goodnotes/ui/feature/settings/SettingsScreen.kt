package com.xbot.goodnotes.ui.feature.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.goodnotes.R
import com.xbot.ui.component.SegmentedButton
import com.xbot.ui.component.SegmentedButtonDefaults
import com.xbot.ui.component.SingleChoiceSegmentedButtonRow
import com.xbot.ui.theme.supportsDynamicTheming

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    state: SettingsScreenState,
    onAction: (SettingsScreenAction) -> Unit
) {
    Column(modifier = modifier) {
        SettingsTitle(
            text = stringResource(R.string.settings_title)
        )
        SettingsThemeSegmentedButton(
            options = stringArrayResource(R.array.settings_options_app_theme).toList(),
            isThemeSelected = { themeId ->
                state.appTheme == themeId
            },
            onThemeSelect = { themeId ->
                onAction(SettingsScreenAction.ChangeAppTheme(themeId))
            }
        )
        Divider(
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        SettingsDynamicThemeSwitch(
            checked = state.useDynamicTheme,
            onCheckedChange = { checked ->
                onAction(SettingsScreenAction.SwitchDynamicTheme(checked))
            }
        )
    }
}

@Composable
private fun SettingsTitle(
    text: String,
    style: TextStyle = MaterialTheme.typography.headlineSmall
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(42.dp)
    ) {
        Text(
            text = text,
            style = style
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsThemeSegmentedButton(
    modifier: Modifier = Modifier,
    options: List<String>,
    isThemeSelected: (Int) -> Boolean,
    onThemeSelect: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    onThemeSelect(index)
                },
                selected = isThemeSelected(index),
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
private fun SettingsDynamicThemeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = supportsDynamicTheming(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Surface(
        onClick = {
            onCheckedChange(!checked)
        },
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_option_dynamic_color),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        onCheckedChange(!checked)
                    },
                    enabled = enabled,
                    interactionSource = interactionSource
                )
            }
        )
    }
}