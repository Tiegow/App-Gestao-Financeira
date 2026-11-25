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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.ufrn.gerfin.app.data.CategoriaDto
import br.ufrn.gerfin.app.data.LancamentoRepository
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
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

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
                    LancamentosViewModel(LancamentoRepository())
                }
            val uiState by lancamentoView.state.collectAsStateWithLifecycle()
            val mesAtualNome by lancamentoView.mesAtualNome.collectAsStateWithLifecycle()

            var categorias by remember {
                mutableStateOf<List<CategoriaDto>>(emptyList())
            }

            val repository = remember { LancamentoRepository() }

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
                    )
                }

                composable<NovaTransacaoRoute> {
                    LaunchedEffect(Unit) {
                        try {
                            categorias = repository.categorias()
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            println("Erro meu chapa.")
                        }
                    }

                    NovaTransacaoScreen(
                        categorias = categorias,
                        onSalvar = { novaTransacao ->
                            val usuarioId = supabase.auth.currentUserOrNull()?.id
                            if (usuarioId == null) {
                                mensagemErro = "Usuário não autenticado"
                            } else {
                                lancamentoView.salvarLancamento(usuarioId, novaTransacao)
                                navController.popBackStack()
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
