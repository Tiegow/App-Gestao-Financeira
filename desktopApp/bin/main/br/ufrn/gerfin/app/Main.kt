package br.ufrn.gerfin.app

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

private const val MIN_WINDOW_WIDTH = 400

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "GerenciadorFinanceiro",
        ) {
            LaunchedEffect(window) {
                window.minimumSize = Dimension(MIN_WINDOW_WIDTH, 0)
            }

            App()
        }
    }
}
