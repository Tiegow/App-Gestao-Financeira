package br.ufrn.gerfin.app.di

import br.ufrn.gerfin.app.data.SupabaseAuthRepository
import br.ufrn.gerfin.app.data.SupabaseLancamentoRepository
import br.ufrn.gerfin.app.domain.repository.AuthRepository
import br.ufrn.gerfin.app.domain.repository.LancamentoRepository
import br.ufrn.gerfin.app.presentation.viewmodel.AuthViewModel
import br.ufrn.gerfin.app.presentation.viewmodel.LancamentosViewModel

interface AppContainer {
    val authRepository: AuthRepository
    val lancamentoRepository: LancamentoRepository

    fun criarAuthViewModel(): AuthViewModel

    fun criarLancamentosViewModel(): LancamentosViewModel
}

class DefaultAppContainer(
    override val authRepository: AuthRepository = SupabaseAuthRepository(),
    override val lancamentoRepository: LancamentoRepository = SupabaseLancamentoRepository(),
) : AppContainer {
    override fun criarAuthViewModel(): AuthViewModel = AuthViewModel(authRepository)

    override fun criarLancamentosViewModel(): LancamentosViewModel = LancamentosViewModel(lancamentoRepository)
}
