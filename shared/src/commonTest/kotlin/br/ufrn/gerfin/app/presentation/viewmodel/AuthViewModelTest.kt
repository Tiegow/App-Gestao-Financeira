package br.ufrn.gerfin.app.presentation.viewmodel

import app.cash.turbine.test
import br.ufrn.gerfin.app.fakes.FakeAuthRepository
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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
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
    fun `login valido marca loginConcluido no estado`() =
        runTest {
            val viewModel = AuthViewModel(FakeAuthRepository())

            viewModel.state.test {
                val inicial = awaitItem()
                assertFalse(inicial.loginConcluido)

                viewModel.entrar("usuario@email.com", "123456")
                advanceUntilIdle()

                val carregando = awaitItem()
                assertTrue(carregando.isCarregando)

                val concluido = awaitItem()
                assertFalse(concluido.isCarregando)
                assertTrue(concluido.loginConcluido)
                assertNull(concluido.mensagemErro)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `login com email vazio emite mensagem de validacao do dominio`() =
        runTest {
            val viewModel = AuthViewModel(FakeAuthRepository())

            viewModel.state.test {
                awaitItem()

                viewModel.entrar("", "123456")
                advanceUntilIdle()

                awaitItem() // carregando
                val erro = awaitItem()
                assertFalse(erro.loginConcluido)
                assertEquals("Você deve informar o e-mail!", erro.mensagemErro)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `registro valido marca cadastroConcluido no estado`() =
        runTest {
            val viewModel = AuthViewModel(FakeAuthRepository())

            viewModel.state.test {
                awaitItem()

                viewModel.registrar("usuario@email.com", "123456", "123456")
                advanceUntilIdle()

                awaitItem() // carregando
                val concluido = awaitItem()
                assertTrue(concluido.cadastroConcluido)
                assertNull(concluido.mensagemErro)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
