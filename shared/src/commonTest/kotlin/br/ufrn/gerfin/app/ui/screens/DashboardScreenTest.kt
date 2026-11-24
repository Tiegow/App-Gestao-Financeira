package br.ufrn.gerfin.app.ui.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import br.ufrn.gerfin.app.ui.components.LancamentosContent
import br.ufrn.gerfin.app.ui.state.LancamentosUiState
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class DashboardScreenTest {
    @Test
    fun `exibe mensagem quando lista esta vazia`() =
        runComposeUiTest {
            setContent {
                LancamentosContent(
                    state = LancamentosUiState.Vazio,
                    onTentarNovamente = {},
                )
            }

            onNodeWithText("Nenhum lançamento encontrado")
                .assertIsDisplayed()
        }

    @Test
    fun `exibe erro e permite tentar novamente`() =
        runComposeUiTest {
            var tentouNovamente = false

            setContent {
                LancamentosContent(
                    state =
                        LancamentosUiState.Erro(
                            "Não foi possível carregar os lançamentos.",
                        ),
                    onTentarNovamente = {
                        tentouNovamente = true
                    },
                )
            }

            onNodeWithText("Não foi possível carregar os lançamentos.")
                .assertIsDisplayed()

            onNodeWithText("Tentar novamente").performClick()

            assertTrue(tentouNovamente)
        }
}
