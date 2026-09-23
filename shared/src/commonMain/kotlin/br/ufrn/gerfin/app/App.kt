package br.ufrn.gerfin.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.navigation.DashboardRoute
import br.ufrn.gerfin.app.navigation.NovaTransacaoRoute
import br.ufrn.gerfin.app.ui.screens.DashboardScreen
import br.ufrn.gerfin.app.ui.screens.NovaTransacaoScreen

@Composable
@Preview
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun App() {
    MaterialTheme {
        var transacoes by remember {
            mutableStateOf(
                listOf(
                    Transacao("1", "3.000,00", true, "01/10/2026", "Salário", "Pix", "Pagamento mensal"),
                    Transacao("2", "450,00", false, "05/10/2026", "Alimentação", "Cartão de Crédito", "Supermercado"),
                    Transacao("3", "120,00", false, "10/10/2026", "Moradia", "Boleto", "Conta de Luz"),
                ),
            )
        }

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
        ) {
            composable<DashboardRoute> {
                DashboardScreen(
                    saldo = "2.430,00",
                    transacoes = transacoes,
                    onNovaTransacao = {
                        navController.navigate(NovaTransacaoRoute)
                    },
                )
            }

            composable<NovaTransacaoRoute> {
                NovaTransacaoScreen(
                    onSalvar = { novaTransacao ->
                        transacoes = listOf(novaTransacao) + transacoes
                        navController.popBackStack()
                    },
                    onCancelar = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}
