package br.ufrn.gerfin.app.presentation.viewmodel

import app.cash.turbine.test
import br.ufrn.gerfin.app.domain.error.LancamentoFailure
import br.ufrn.gerfin.app.fakes.FakeLancamentoRepository
import br.ufrn.gerfin.app.fixtures.transacaoFake
import br.ufrn.gerfin.app.presentation.state.LancamentosUiState
import br.ufrn.gerfin.app.presentation.state.RelatorioAnualUiState
import br.ufrn.gerfin.app.presentation.state.TipoFalhaLancamentos
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
                        transacoes = listOf(transacaoFake()),
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
    fun `falha de conexao tem estado e mensagem especificos`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        excecao = LancamentoFailure.SemConexao(IllegalStateException("offline")),
                    ),
                )

            viewModel.state.test {
                awaitItem()
                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                val erro = assertIs<LancamentosUiState.Erro>(awaitItem())
                assertEquals(TipoFalhaLancamentos.CONEXAO, erro.tipo)
                assertEquals(
                    "Sem conexão. Verifique sua internet e tente novamente.",
                    erro.mensagem,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `resposta invalida tem estado e mensagem especificos`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        excecao = LancamentoFailure.RespostaInvalida(IllegalStateException("invalid response")),
                    ),
                )

            viewModel.state.test {
                awaitItem()
                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                val erro = assertIs<LancamentosUiState.Erro>(awaitItem())
                assertEquals(TipoFalhaLancamentos.RESPOSTA_INVALIDA, erro.tipo)
                assertEquals(
                    "O servidor retornou uma resposta inválida. Tente novamente.",
                    erro.mensagem,
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
                                Result.success(listOf(transacaoFake())),
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
                        transacoes = listOf(transacaoFake()),
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

    @Test
    fun `falha ao salvar exibe aviso e preserva os lancamentos carregados`() =
        runTest {
            val transacaoExistente = transacaoFake(id = "existente")
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        transacoes = listOf(transacaoExistente),
                        excecaoInserir =
                            LancamentoFailure.SemConexao(
                                IllegalStateException("offline"),
                            ),
                    ),
                )

            viewModel.state.test {
                awaitItem()
                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()
                val estadoAntes =
                    assertIs<LancamentosUiState.Sucesso>(awaitItem())

                viewModel.avisoOperacao.test {
                    assertEquals(null, awaitItem())
                    viewModel.salvarLancamento("usuario-1", transacaoFake(id = "novo"))
                    advanceUntilIdle()

                    assertEquals(
                        "Sem conexão. O lançamento não foi salvo.",
                        awaitItem(),
                    )
                    cancelAndIgnoreRemainingEvents()
                }

                val estadoDepois = assertIs<LancamentosUiState.Sucesso>(viewModel.state.value)
                assertEquals(estadoAntes.transacoes, estadoDepois.transacoes)
                assertEquals(listOf("existente"), estadoDepois.transacoes.map { it.id })
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `filtra lancamentos por mes ao navegar entre meses`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        transacoes = listOf(transacaoFake()),
                    ),
                )

            viewModel.state.test {
                assertIs<LancamentosUiState.Carregando>(awaitItem())

                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                val outubro = assertIs<LancamentosUiState.Sucesso>(awaitItem())
                assertEquals(1, outubro.transacoes.size)
                assertEquals("-10,00", outubro.saldoEstimado)

                viewModel.proximoMes()
                assertIs<LancamentosUiState.Vazio>(awaitItem())

                viewModel.mesAnterior()
                val voltaOutubro = assertIs<LancamentosUiState.Sucesso>(awaitItem())
                assertEquals(1, voltaOutubro.transacoes.size)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `consolida relatorio anual dos 12 meses e permite trocar de ano`() =
        runTest {
            val viewModel =
                LancamentosViewModel(
                    FakeLancamentoRepository(
                        transacoes = listOf(transacaoFake()),
                    ),
                )

            viewModel.relatorioAnualState.test {
                assertIs<RelatorioAnualUiState.Carregando>(awaitItem())

                viewModel.listarLancamentos("usuario-1")
                advanceUntilIdle()

                val ano2026 = assertIs<RelatorioAnualUiState.Sucesso>(awaitItem())
                assertEquals(12, ano2026.meses.size)
                assertEquals("0,00", ano2026.totalEntradas)
                assertEquals("10,00", ano2026.totalSaidas)
                assertEquals("-10,00", ano2026.saldoAcumulado)

                viewModel.proximoAnoRelatorio()
                assertEquals(2027, viewModel.anoRelatorio.value)
                assertIs<RelatorioAnualUiState.Vazio>(awaitItem())

                viewModel.anoRelatorioAnterior()
                assertEquals(2026, viewModel.anoRelatorio.value)
                val volta2026 = assertIs<RelatorioAnualUiState.Sucesso>(awaitItem())
                assertEquals("-10,00", volta2026.saldoAcumulado)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
