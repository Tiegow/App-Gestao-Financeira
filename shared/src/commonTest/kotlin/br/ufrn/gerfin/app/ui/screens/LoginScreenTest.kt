package br.ufrn.gerfin.app.ui.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class LoginScreenTest {
    @Test
    fun `exibe tela de login`() =
        runComposeUiTest {
            setContent {
                LoginScreen(
                    onEntrar = { _, _ -> },
                    onRegister = {},
                    mensagemErro = null,
                )
            }

            onNodeWithText("Login").assertIsDisplayed()
            onNodeWithText("Entrar").assertIsDisplayed()
            onNodeWithText("Não tem conta? Registre-se").assertIsDisplayed()
        }

    @Test
    fun `envia email e senha ao clicar em entrar`() =
        runComposeUiTest {
            var emailRecebido = ""
            var senhaRecebida = ""

            setContent {
                LoginScreen(
                    onEntrar = { email, senha ->
                        emailRecebido = email
                        senhaRecebida = senha
                    },
                    onRegister = {},
                    mensagemErro = null,
                )
            }

            onNode(hasText("Digite seu E-mail"))
                .performTextInput("usuario@email.com")

            onNode(hasText("Digite sua senha"))
                .performTextInput("123456")

            onNodeWithText("Entrar").performClick()
            waitForIdle()

            assertEquals("usuario@email.com", emailRecebido)
            assertEquals("123456", senhaRecebida)
        }
}
