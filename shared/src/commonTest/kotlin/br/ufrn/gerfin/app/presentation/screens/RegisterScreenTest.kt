package br.ufrn.gerfin.app.presentation.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class RegisterScreenTest {
    @Test
    fun `exibe tela de cadastro`() =
        runComposeUiTest {
            setContent {
                RegisterScreen(
                    onRegister = { _, _, _ -> },
                    onVoltar = {},
                    mensagemErro = null,
                )
            }

            onNodeWithTag("botao-registrar").assertIsDisplayed()
            onNodeWithText("Digite seu E-mail").assertIsDisplayed()
            onNodeWithText("Digite sua senha").assertIsDisplayed()
            onNodeWithText("Digite novamente sua senha").assertIsDisplayed()
        }

    @Test
    fun `envia dados preenchidos no cadastro`() =
        runComposeUiTest {
            var emailRecebido = ""
            var senha01Recebida = ""
            var senha02Recebida = ""

            setContent {
                RegisterScreen(
                    onRegister = { email, senha01, senha02 ->
                        emailRecebido = email
                        senha01Recebida = senha01
                        senha02Recebida = senha02
                    },
                    onVoltar = {},
                    mensagemErro = null,
                )
            }

            onNodeWithText("Digite seu E-mail")
                .performTextInput("usuario@email.com")

            onNodeWithText("Digite sua senha")
                .performTextInput("123456")

            onNodeWithText("Digite novamente sua senha")
                .performTextInput("123456")

            onNodeWithTag("botao-registrar").performClick()
            waitForIdle()

            assertEquals("usuario@email.com", emailRecebido)
            assertEquals("123456", senha01Recebida)
            assertEquals("123456", senha02Recebida)
        }
}
