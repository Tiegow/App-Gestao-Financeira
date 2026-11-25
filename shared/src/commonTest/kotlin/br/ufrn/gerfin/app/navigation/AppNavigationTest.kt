package br.ufrn.gerfin.app.navigation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.ufrn.gerfin.app.ui.screens.DashboardScreen
import br.ufrn.gerfin.app.ui.screens.NovaTransacaoScreen
import br.ufrn.gerfin.app.ui.screens.RelatorioAnualScreen
import br.ufrn.gerfin.app.ui.state.LancamentosUiState
import br.ufrn.gerfin.app.ui.state.RelatorioAnualUiState
import br.ufrn.gerfin.app.ui.theme.GestFinTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppNavigationTest {
    @Test
    fun `navega do dashboard para relatorio anual e volta ao clicar em voltar`() =
        runComposeUiTest {
            setContent {
                GestFinTheme {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = DashboardRoute,
                    ) {
                        composable<DashboardRoute> {
                            DashboardScreen(
                                uiState = LancamentosUiState.Vazio,
                                mesAtualNome = "Outubro 2026",
                                onMesAnterior = {},
                                onProximoMes = {},
                                onTentarNovamente = {},
                                onNovaTransacao = { navController.navigate(NovaTransacaoRoute) },
                                onAbrirRelatorioAnual = { navController.navigate(RelatorioAnualRoute) },
                            )
                        }
                        composable<RelatorioAnualRoute> {
                            RelatorioAnualScreen(
                                uiState = RelatorioAnualUiState.Vazio,
                                anoSelecionado = 2026,
                                onAnoAnterior = {},
                                onProximoAno = {},
                                onTentarNovamente = {},
                                onVoltar = { navController.popBackStack() },
                            )
                        }
                    }
                }
            }

            onNodeWithText("GestFin").assertIsDisplayed()

            onNodeWithText("Relatório Anual").performClick()
            waitForIdle()

            onNodeWithText("Saldo Acumulado no Ano").assertIsDisplayed()

            onNodeWithContentDescription("Voltar").performClick()
            waitForIdle()

            onNodeWithText("GestFin").assertIsDisplayed()
        }

    @Test
    fun `navega do dashboard para nova transacao e desempilha ao cancelar`() =
        runComposeUiTest {
            setContent {
                GestFinTheme {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = DashboardRoute,
                    ) {
                        composable<DashboardRoute> {
                            DashboardScreen(
                                uiState = LancamentosUiState.Vazio,
                                mesAtualNome = "Outubro 2026",
                                onMesAnterior = {},
                                onProximoMes = {},
                                onTentarNovamente = {},
                                onNovaTransacao = { navController.navigate(NovaTransacaoRoute) },
                            )
                        }
                        composable<NovaTransacaoRoute> {
                            NovaTransacaoScreen(
                                categorias = emptyList(),
                                onSalvar = {},
                                onCancelar = { navController.popBackStack() },
                            )
                        }
                    }
                }
            }

            onNodeWithText("GestFin").assertIsDisplayed()

            onNodeWithContentDescription("Adicionar nova transação").performClick()
            waitForIdle()

            onNodeWithText("Nova Transação").assertIsDisplayed()

            onNodeWithText("Cancelar").performClick()
            waitForIdle()

            onNodeWithText("GestFin").assertIsDisplayed()
        }
}
