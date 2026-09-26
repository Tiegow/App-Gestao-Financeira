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
import kotlin.math.round

private fun parseValor(valorStr: String): Double {
    val limpo = valorStr.replace(".", "").replace(",", ".")
    return limpo.toDoubleOrNull() ?: 0.0
}

private fun formatarValor(valor: Double): String {
    val intPart = valor.toLong()
    val cents = round((valor - intPart) * 100).toInt().toString().padStart(2, '0')
    val intFormatado =
        intPart
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    return "$intFormatado,$cents"
}

@Composable
@Preview
@Suppress("FunctionNaming", "ktlint:standard:function-naming", "LongMethod")
fun App() {
    MaterialTheme {
        var transacoes by remember {
            mutableStateOf(
                listOf(
                    Transacao("1", "3.000,00", true, "01/10/2026", "Salário", "Pix", "Pagamento mensal", true),
                    Transacao("2", "450,00", false, "05/10/2026", "Alimentação", "Cartão de Crédito", "Supermercado", true),
                    Transacao("3", "120,00", false, "10/11/2026", "Moradia", "Boleto", "Conta de Luz futura", false),
                ),
            )
        }

        var mesAtual by remember { mutableStateOf(10) }
        var anoAtual by remember { mutableStateOf(2026) }

        val mesesNomes =
            listOf(
                "",
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro",
            )

        // Filtra transações do mês
        val transacoesDoMes =
            transacoes.filter {
                val parts = it.data.split("/")
                if (parts.size == 3) {
                    val m = parts[1].toIntOrNull()
                    val y = parts[2].toIntOrNull()
                    m == mesAtual && y == anoAtual
                } else {
                    false
                }
            }

        val totalReceitas = transacoesDoMes.filter { it.isReceita }.sumOf { parseValor(it.valor) }
        val totalDespesasPagas = transacoesDoMes.filter { !it.isReceita && it.isPago }.sumOf { parseValor(it.valor) }
        val totalDespesasAberto = transacoesDoMes.filter { !it.isReceita && !it.isPago }.sumOf { parseValor(it.valor) }

        val saldoMes = totalReceitas - totalDespesasPagas - totalDespesasAberto

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
        ) {
            composable<DashboardRoute> {
                DashboardScreen(
                    mesAtualNome = "${mesesNomes[mesAtual]} $anoAtual",
                    onMesAnterior = {
                        if (mesAtual == 1) {
                            mesAtual = 12
                            anoAtual--
                        } else {
                            mesAtual--
                        }
                    },
                    onProximoMes = {
                        if (mesAtual == 12) {
                            mesAtual = 1
                            anoAtual++
                        } else {
                            mesAtual++
                        }
                    },
                    saldoEstimado = formatarValor(saldoMes),
                    receitas = formatarValor(totalReceitas),
                    despesasPagas = formatarValor(totalDespesasPagas),
                    despesasAberto = formatarValor(totalDespesasAberto),
                    transacoes = transacoesDoMes,
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
