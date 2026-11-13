package br.ufrn.gerfin.app.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufrn.gerfin.app.data.LancamentoRepositoryContract
import br.ufrn.gerfin.app.data.toInsertDto
import br.ufrn.gerfin.app.data.toTransacao
import br.ufrn.gerfin.app.model.Transacao
import kotlinx.coroutines.launch
import br.ufrn.gerfin.app.ui.state.LancamentosUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LancamentosViewModel(
    private val repository: LancamentoRepositoryContract,
) : ViewModel() {
    private val _state =
        MutableStateFlow<LancamentosUiState>(
            LancamentosUiState.Carregando,
        )

    val state: StateFlow<LancamentosUiState> = _state.asStateFlow()

    var transacoes by mutableStateOf<List<Transacao>>(emptyList())
        private set

    fun listarLancamentos(usuarioId: String) {
        viewModelScope.launch {
            _state.value = LancamentosUiState.Carregando

            try {
                val categorias = repository.categorias()
                val lancamentos = repository.listar(usuarioId)

                transacoes = lancamentos.map { it.toTransacao(categorias) }

                _state.value =
                    if (transacoes.isEmpty()) {
                        LancamentosUiState.Vazio
                    } else {
                        LancamentosUiState.Sucesso(transacoes)
                    }
            } catch (exception: Exception) {
                _state.value =
                    LancamentosUiState.Erro(
                        "Não foi possível carregar os lançamentos.",
                    )
            }
        }
    }

    suspend fun salvarLancamento(
        usuarioId: String,
        transacao: Transacao,
    ) {
        val categorias = repository.categorias()
        val dto = transacao.toInsertDto(usuarioId, categorias)
        val salvo = repository.inserir(dto)

        transacoes = listOf(salvo.toTransacao(categorias)) + transacoes
    }
}
