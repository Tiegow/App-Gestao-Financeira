package br.ufrn.gerfin.app

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "GerenciadorFinanceiro",
        ) {
            LaunchedEffect(window) {
                window.minimumSize = Dimension(400, 0)
            }

            App()
        }
    }
}
