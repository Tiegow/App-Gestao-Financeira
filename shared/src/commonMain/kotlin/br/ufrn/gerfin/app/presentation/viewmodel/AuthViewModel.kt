package br.ufrn.gerfin.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufrn.gerfin.app.domain.error.CadastroException
import br.ufrn.gerfin.app.domain.repository.AuthRepository
import br.ufrn.gerfin.app.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun entrar(
        email: String,
        senha: String,
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isCarregando = true, mensagemErro = null) }
            try {
                authRepository.login(email, senha)
                _state.update { it.copy(isCarregando = false, loginConcluido = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: CadastroException) {
                _state.update { it.copy(isCarregando = false, mensagemErro = e.message) }
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        isCarregando = false,
                        mensagemErro = "Não foi possível realizar o login!",
                    )
                }
            }
        }
    }

    fun registrar(
        email: String,
        senha01: String,
        senha02: String,
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isCarregando = true, mensagemErro = null) }
            try {
                authRepository.registrar(email, senha01, senha02)
                _state.update { it.copy(isCarregando = false, cadastroConcluido = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: CadastroException) {
                _state.update { it.copy(isCarregando = false, mensagemErro = e.message) }
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        isCarregando = false,
                        mensagemErro = "Não foi possível realizar o cadastro!",
                    )
                }
            }
        }
    }

    fun limparErro() {
        _state.update { it.copy(mensagemErro = null) }
    }

    fun consumirLoginConcluido() {
        _state.update { it.copy(loginConcluido = false) }
    }

    fun consumirCadastroConcluido() {
        _state.update { it.copy(cadastroConcluido = false) }
    }

    fun usuarioAtualId(): String? = authRepository.usuarioAtualId()
}
