package br.ufrn.gerfin.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.ui.screens.DashboardScreen

@Composable
@Preview
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun App() {
    MaterialTheme {
        var transacoes by remember {
            mutableStateOf(
                listOf(
                    Transacao("1", "Salário", "3.000,00", true),
                    Transacao("2", "Supermercado", "450,00", false),
                    Transacao("3", "Conta de Luz", "120,00", false),
                ),
            )
        }

        DashboardScreen(
            saldo = "2.430,00",
            transacoes = transacoes,
            onNovaTransacao = {
                // Simulação: adiciona uma transação de teste ao clicar no +
                val nova =
                    Transacao(
                        id =
                            kotlin.random.Random
                                .nextInt()
                                .toString(),
                        descricao = "Nova Transação",
                        valor = "100,00",
                        isReceita = false,
                    )
                transacoes = listOf(nova) + transacoes
            },
        )
    }
}
