package com.xbot.goodnotes

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.xbot.goodnotes.ui.FastOutLinearInInterpolator
import com.xbot.goodnotes.ui.GoodNotesApp
import com.xbot.goodnotes.ui.enableEdgeToEdge
import com.xbot.goodnotes.ui.shouldUseDarkTheme
import com.xbot.goodnotes.ui.shouldUseDynamicTheming
import com.xbot.ui.theme.GoodNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            onSplashScreenExit(splashScreenViewProvider)
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        enableEdgeToEdge()

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge { darkTheme }
                onDispose {}
            }

            GoodNotesTheme(
                darkTheme = darkTheme,
                dynamicColor = shouldUseDynamicTheming(uiState)
            ) {
                GoodNotesApp()
            }
        }
    }

    private fun onSplashScreenExit(splashScreenViewProvider: SplashScreenViewProvider) {
        val accelerateInterpolator = FastOutLinearInInterpolator()
        val splashScreenView = splashScreenViewProvider.view
        val iconView = splashScreenViewProvider.iconView

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f),
            ObjectAnimator.ofFloat(iconView, View.ALPHA, 1f, 0f)
        )
        animatorSet.duration = SPLASHSCREEN_ALPHA_ANIMATION_DURATION
        animatorSet.interpolator = accelerateInterpolator

        animatorSet.doOnEnd { splashScreenViewProvider.remove() }
        animatorSet.start()
    }

    companion object {
        private const val SPLASHSCREEN_ALPHA_ANIMATION_DURATION = 200L
    }
}
