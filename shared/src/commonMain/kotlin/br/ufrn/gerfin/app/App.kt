package br.ufrn.gerfin.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.ufrn.gerfin.app.di.AppContainer
import br.ufrn.gerfin.app.di.DefaultAppContainer
import br.ufrn.gerfin.app.presentation.navigation.DashboardRoute
import br.ufrn.gerfin.app.presentation.navigation.LoginRoute
import br.ufrn.gerfin.app.presentation.navigation.NovaTransacaoRoute
import br.ufrn.gerfin.app.presentation.navigation.RegisterRoute
import br.ufrn.gerfin.app.presentation.navigation.RelatorioAnualRoute
import br.ufrn.gerfin.app.presentation.screens.DashboardScreen
import br.ufrn.gerfin.app.presentation.screens.LoginScreen
import br.ufrn.gerfin.app.presentation.screens.NovaTransacaoScreen
import br.ufrn.gerfin.app.presentation.screens.RegisterScreen
import br.ufrn.gerfin.app.presentation.screens.RelatorioAnualScreen
import br.ufrn.gerfin.app.presentation.theme.GestFinTheme
import br.ufrn.gerfin.app.presentation.viewmodel.AuthViewModel
import br.ufrn.gerfin.app.presentation.viewmodel.LancamentosViewModel

private val defaultAppContainer: AppContainer by lazy { DefaultAppContainer() }

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming", "LongMethod")
fun App(
    darkTheme: Boolean = isSystemInDarkTheme(),
    container: AppContainer = defaultAppContainer,
) {
    GestFinTheme(darkTheme = darkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val navController = rememberNavController()

            val authViewModel: AuthViewModel =
                viewModel {
                    container.criarAuthViewModel()
                }
            val lancamentoView: LancamentosViewModel =
                viewModel {
                    container.criarLancamentosViewModel()
                }

            val authUiState by authViewModel.state.collectAsStateWithLifecycle()
            val uiState by lancamentoView.state.collectAsStateWithLifecycle()
            val mesAtualNome by lancamentoView.mesAtualNome.collectAsStateWithLifecycle()
            val relatorioAnualState by lancamentoView.relatorioAnualState.collectAsStateWithLifecycle()
            val anoRelatorio by lancamentoView.anoRelatorio.collectAsStateWithLifecycle()
            val categorias by lancamentoView.categorias.collectAsStateWithLifecycle()
            val avisoOperacao by lancamentoView.avisoOperacao.collectAsStateWithLifecycle()

            NavHost(
                navController = navController,
                startDestination = LoginRoute,
            ) {
                composable<LoginRoute> {
                    LaunchedEffect(authUiState.loginConcluido) {
                        if (authUiState.loginConcluido) {
                            authViewModel.consumirLoginConcluido()
                            navController.navigate(DashboardRoute)
                        }
                    }

                    LoginScreen(
                        mensagemErro = authUiState.mensagemErro,
                        onEntrar = { email, senha ->
                            authViewModel.entrar(email, senha)
                        },
                        onRegister = {
                            authViewModel.limparErro()
                            navController.navigate(RegisterRoute)
                        },
                    )
                }

                composable<RegisterRoute> {
                    LaunchedEffect(authUiState.cadastroConcluido) {
                        if (authUiState.cadastroConcluido) {
                            authViewModel.consumirCadastroConcluido()
                            navController.popBackStack()
                        }
                    }

                    RegisterScreen(
                        mensagemErro = authUiState.mensagemErro,
                        onRegister = { email, senha01, senha02 ->
                            authViewModel.registrar(email, senha01, senha02)
                        },
                        onVoltar = {
                            authViewModel.limparErro()
                            navController.navigate(LoginRoute)
                        },
                    )
                }

                composable<DashboardRoute> {
                    val usuarioId = authViewModel.usuarioAtualId()

                    LaunchedEffect(usuarioId) {
                        if (usuarioId != null) {
                            lancamentoView.listarLancamentos(usuarioId)
                        }
                    }

                    DashboardScreen(
                        uiState = uiState,
                        mesAtualNome = mesAtualNome,
                        onMesAnterior = { lancamentoView.mesAnterior() },
                        onProximoMes = { lancamentoView.proximoMes() },
                        onTentarNovamente = {
                            if (usuarioId != null) {
                                lancamentoView.listarLancamentos(usuarioId)
                            }
                        },
                        onNovaTransacao = {
                            navController.navigate(NovaTransacaoRoute)
                        },
                        onAbrirRelatorioAnual = {
                            navController.navigate(RelatorioAnualRoute)
                        },
                        avisoOperacao = avisoOperacao,
                        onAvisoOperacaoExibido = {
                            lancamentoView.limparAvisoOperacao()
                        },
                    )
                }

                composable<RelatorioAnualRoute> {
                    val usuarioId = authViewModel.usuarioAtualId()

                    RelatorioAnualScreen(
                        uiState = relatorioAnualState,
                        anoSelecionado = anoRelatorio,
                        onAnoAnterior = { lancamentoView.anoRelatorioAnterior() },
                        onProximoAno = { lancamentoView.proximoAnoRelatorio() },
                        onTentarNovamente = {
                            if (usuarioId != null) {
                                lancamentoView.listarLancamentos(usuarioId)
                            }
                        },
                        onVoltar = {
                            navController.popBackStack()
                        },
                    )
                }

                composable<NovaTransacaoRoute> {
                    LaunchedEffect(Unit) {
                        lancamentoView.carregarCategorias()
                    }

                    NovaTransacaoScreen(
                        categorias = categorias,
                        avisoOperacao = avisoOperacao,
                        onTentarCarregarCategorias = {
                            lancamentoView.carregarCategorias()
                        },
                        onSalvar = { novaTransacao ->
                            val usuarioId = authViewModel.usuarioAtualId()
                            if (usuarioId != null) {
                                lancamentoView.salvarLancamento(usuarioId, novaTransacao)
                                navController.popBackStack()
                            }
                        },
                        onCancelar = {
                            lancamentoView.limparAvisoOperacao()
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
