package br.ufrn.gerfin.app.di

import br.ufrn.gerfin.app.fakes.FakeAuthRepository
import br.ufrn.gerfin.app.fakes.FakeLancamentoRepository
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class AppContainerTest {
    @Test
    fun `monta modulo padrao de injecao de dependencias sem erros`() {
        val container: AppContainer = DefaultAppContainer()

        assertNotNull(container.authRepository)
        assertNotNull(container.lancamentoRepository)
        assertNotNull(container.criarAuthViewModel())
        assertNotNull(container.criarLancamentosViewModel())
    }

    @Test
    fun `permite substituir repositorios por dubles no container`() {
        val fakeAuth = FakeAuthRepository()
        val fakeLancamentos = FakeLancamentoRepository()
        val container: AppContainer =
            DefaultAppContainer(
                authRepository = fakeAuth,
                lancamentoRepository = fakeLancamentos,
            )

        assertSame(fakeAuth, container.authRepository)
        assertSame(fakeLancamentos, container.lancamentoRepository)
        assertNotNull(container.criarAuthViewModel())
        assertNotNull(container.criarLancamentosViewModel())
    }
}
