package com.ponyu.weather.feature.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_showsSplashImage_andTriggersOnShowHome() {
        var isHomeShown = false

        // Запускаем SplashScreen и отслеживаем вызов onShowHome
        composeTestRule.setContent {
            SplashScreen(onShowHome = {
                isHomeShown = true
            })
        }

        // Проверяем, что изображение отображается
        composeTestRule
            .onNodeWithContentDescription("Splash Weather")
            .assertIsDisplayed()

        // Эмулируем задержку и проверяем, что анимация выполнена и вызвана onShowHome
        composeTestRule.mainClock.advanceTimeBy(3000L)

        assert(isHomeShown)
    }
}