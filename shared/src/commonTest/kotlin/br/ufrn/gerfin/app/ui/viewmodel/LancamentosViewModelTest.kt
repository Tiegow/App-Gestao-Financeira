package br.ufrn.gerfin.app.ui.viewmodel

import app.cash.turbine.test
import br.ufrn.gerfin.app.fakes.FakeLancamentoRepository
import br.ufrn.gerfin.app.fixtures.lancamentoDtoFake
import br.ufrn.gerfin.app.ui.state.LancamentosUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class LancamentosViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun configurarDispatcher() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun restaurarDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carrega lancamentos com sucesso`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        lancamentos = listOf(lancamentoDtoFake()),
                    ),
                )

            viewModel.state.test {
                assertIs<LancamentosUiState.Carregando>(
                    awaitItem(),
                )

                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                assertIs<LancamentosUiState.Sucesso>(
                    awaitItem(),
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `lista vazia resulta em estado vazio`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(),
                )

            viewModel.state.test {
                assertIs<LancamentosUiState.Carregando>(awaitItem())

                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                assertIs<LancamentosUiState.Vazio>(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `falha do repositorio resulta em estado de erro`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        excecao = IllegalStateException("Falha"),
                    ),
                )

            viewModel.state.test {
                assertIs<LancamentosUiState.Carregando>(awaitItem())

                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                val estado = assertIs<LancamentosUiState.Erro>(awaitItem())
                assertEquals(
                    "Não foi possível carregar os lançamentos.",
                    estado.mensagem,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `tentar novamente executa uma nova busca`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        respostas =
                            listOf(
                                Result.failure(IllegalStateException("Falha")),
                                Result.success(listOf(lancamentoDtoFake())),
                            ),
                    ),
                )

            viewModel.state.test {
                assertIs<LancamentosUiState.Carregando>(awaitItem())

                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()
                assertIs<LancamentosUiState.Erro>(awaitItem())

                viewModel.listarLancamentos("usuario-1")
                assertIs<LancamentosUiState.Carregando>(awaitItem())
                advanceUntilIdle()

                assertIs<LancamentosUiState.Sucesso>(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `estado sucesso contem os lancamentos carregados`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        lancamentos = listOf(lancamentoDtoFake()),
                    ),
                )

            viewModel.state.test {
                awaitItem()
                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                val estado = assertIs<LancamentosUiState.Sucesso>(awaitItem())
                assertEquals(1, estado.transacoes.size)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
