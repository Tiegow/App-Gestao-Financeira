package br.ufrn.gerfin.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.ufrn.gerfin.app.data.login
import br.ufrn.gerfin.app.data.register
import br.ufrn.gerfin.app.data.supabase
import br.ufrn.gerfin.app.exception.CadastroException
import br.ufrn.gerfin.app.navigation.DashboardRoute
import br.ufrn.gerfin.app.navigation.LoginRoute
import br.ufrn.gerfin.app.navigation.NovaTransacaoRoute
import br.ufrn.gerfin.app.navigation.RegisterRoute
import br.ufrn.gerfin.app.ui.screens.DashboardScreen
import br.ufrn.gerfin.app.ui.screens.LoginScreen
import br.ufrn.gerfin.app.ui.screens.NovaTransacaoScreen
import br.ufrn.gerfin.app.ui.screens.RegisterScreen
import br.ufrn.gerfin.app.ui.theme.GestFinTheme
import br.ufrn.gerfin.app.ui.viewmodel.LancamentosViewModel
import br.ufrn.gerfin.app.utils.formatarValor
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

private fun parseValor(valorStr: String): Double {
    val limpo = valorStr.replace(".", "").replace(",", ".")
    return limpo.toDoubleOrNull() ?: 0.0
}

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming", "LongMethod")
fun App(darkTheme: Boolean = isSystemInDarkTheme()) {
    GestFinTheme(darkTheme = darkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            var mensagemErro by remember { mutableStateOf<String?>(null) }

            val lancamentoView: LancamentosViewModel =
                viewModel {
                    LancamentosViewModel()
                }
            val transacoes = lancamentoView.transacoes

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

            val totalReceitas =
                transacoesDoMes.filter { it.isReceita }.sumOf { parseValor(it.valor) }
            val totalDespesasPagas =
                transacoesDoMes
                    .filter { !it.isReceita && it.isPago }
                    .sumOf { parseValor(it.valor) }
            val totalDespesasAberto =
                transacoesDoMes
                    .filter { !it.isReceita && !it.isPago }
                    .sumOf { parseValor(it.valor) }

            val saldoMes = totalReceitas - totalDespesasPagas - totalDespesasAberto

            NavHost(
                navController = navController,
                startDestination = LoginRoute,
            ) {
                composable<LoginRoute> {
                    LoginScreen(
                        mensagemErro = mensagemErro,
                        onEntrar = { email, senha ->
                            scope.launch {
                                try {
                                    login(email, senha)
                                    navController.navigate(DashboardRoute)
                                } catch (e: Exception) {
                                    mensagemErro =
                                        if (e is CadastroException) {
                                            e.message
                                        } else {
                                            "Não foi possível realizar o login!"
                                        }
                                }
                            }
                        },
                        onRegister = {
                            mensagemErro = ""
                            navController.navigate(RegisterRoute)
                        },
                    )
                }

                composable<RegisterRoute> {
                    RegisterScreen(
                        mensagemErro = mensagemErro,
                        onRegister = { email, senha01, senha02 ->
                            scope.launch {
                                try {
                                    register(email, senha01, senha02)
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    mensagemErro =
                                        if (e is CadastroException) {
                                            e.message
                                        } else {
                                            "Não foi possível realizar o cadastro!"
                                        }
                                }
                            }
                        },
                        onVoltar = {
                            mensagemErro = ""
                            navController.navigate(LoginRoute)
                        },
                    )
                }

                composable<DashboardRoute> {
                    val usuarioId = supabase.auth.currentUserOrNull()?.id

                    LaunchedEffect(usuarioId) {
                        if (usuarioId != null) {
                            lancamentoView.listarLancamentos(usuarioId)
                        }
                    }
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
                            scope.launch {
                                try {
                                    val usuarioId =
                                        supabase.auth.currentUserOrNull()?.id
                                            ?: error("Usuário não autenticado")

                                    lancamentoView.salvarLancamento(usuarioId, novaTransacao)
                                    navController.popBackStack()
                                } catch (e: CancellationException) {
                                    throw e
                                } catch (e: Exception) {
                                    mensagemErro = "Não foi possível salvar o lançamento."
                                }
                            }
                        },
                        onCancelar = {
                            navController.popBackStack()
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun AppLightPreview() {
    App(darkTheme = false)
}

@Preview
@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun AppDarkPreview() {
    App(darkTheme = true)
}
