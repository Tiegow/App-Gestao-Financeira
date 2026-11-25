package br.ufrn.gerfin.app.ui.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import br.ufrn.gerfin.app.ui.state.RelatorioAnualUiState
import br.ufrn.gerfin.app.ui.state.ResumoMensalAnual
import br.ufrn.gerfin.app.ui.theme.GestFinTheme
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class RelatorioAnualScreenTest {
    @Test
    fun `exibe consolidado anual e permite trocar de ano`() =
        runComposeUiTest {
            var clicouAnoAnterior = false
            var clicouProximoAno = false

            setContent {
                GestFinTheme {
                    RelatorioAnualScreen(
                        uiState =
                            RelatorioAnualUiState.Sucesso(
                                totalEntradas = "3.000,00",
                                totalSaidas = "450,00",
                                saldoAcumulado = "2.550,00",
                                meses =
                                    listOf(
                                        ResumoMensalAnual(
                                            mesNumero = 10,
                                            mesNome = "Outubro",
                                            entradas = "3.000,00",
                                            saidas = "450,00",
                                            saldo = "2.550,00",
                                            isSaldoNegativo = false,
                                        ),
                                    ),
                            ),
                        anoSelecionado = 2026,
                        onAnoAnterior = { clicouAnoAnterior = true },
                        onProximoAno = { clicouProximoAno = true },
                        onTentarNovamente = {},
                        onVoltar = {},
                    )
                }
            }

            onNodeWithText("Relatório Anual").assertIsDisplayed()
            onNodeWithText("2026").assertIsDisplayed()
            onNodeWithText("R$ 2.550,00").assertIsDisplayed()

            onNodeWithContentDescription("Ano anterior").performClick()
            onNodeWithContentDescription("Próximo ano").performClick()
            waitForIdle()

            assertTrue(clicouAnoAnterior)
            assertTrue(clicouProximoAno)
        }

    @Test
    fun `exibe mensagem quando ano nao possui lancamentos`() =
        runComposeUiTest {
            setContent {
                GestFinTheme {
                    RelatorioAnualScreen(
                        uiState = RelatorioAnualUiState.Vazio,
                        anoSelecionado = 2025,
                        onAnoAnterior = {},
                        onProximoAno = {},
                        onTentarNovamente = {},
                        onVoltar = {},
                    )
                }
            }

            onNodeWithText("Nenhum lançamento encontrado em 2025.")
                .assertIsDisplayed()
        }

    @Test
    fun `exibe erro e permite tentar de novo no relatorio anual`() =
        runComposeUiTest {
            var tentouNovamente = false

            setContent {
                GestFinTheme {
                    RelatorioAnualScreen(
                        uiState = RelatorioAnualUiState.Erro("Não foi possível carregar os lançamentos."),
                        anoSelecionado = 2026,
                        onAnoAnterior = {},
                        onProximoAno = {},
                        onTentarNovamente = { tentouNovamente = true },
                        onVoltar = {},
                    )
                }
            }

            onNodeWithText("Não foi possível carregar os lançamentos.").assertIsDisplayed()
            onNodeWithText("Tentar de novo").performClick()
            waitForIdle()

            assertTrue(tentouNovamente)
        }
}
