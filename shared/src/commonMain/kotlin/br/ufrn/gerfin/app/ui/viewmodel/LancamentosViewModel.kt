package br.ufrn.gerfin.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufrn.gerfin.app.data.LancamentoRepositoryContract
import br.ufrn.gerfin.app.data.toInsertDto
import br.ufrn.gerfin.app.data.toTransacao
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.ui.state.LancamentosUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LancamentosViewModel(
    private val repository: LancamentoRepositoryContract,
) : ViewModel() {
    private val _state =
        MutableStateFlow<LancamentosUiState>(
            LancamentosUiState.Carregando,
        )

    val state: StateFlow<LancamentosUiState> = _state.asStateFlow()

    private var transacoesAtuais: List<Transacao> = emptyList()

    fun listarLancamentos(usuarioId: String) {
        viewModelScope.launch {
            _state.value = LancamentosUiState.Carregando

            try {
                val categorias = repository.categorias()
                val lancamentos = repository.listar(usuarioId)

                transacoesAtuais = lancamentos.map { it.toTransacao(categorias) }

                _state.value =
                    if (transacoesAtuais.isEmpty()) {
                        LancamentosUiState.Vazio
                    } else {
                        LancamentosUiState.Sucesso(transacoesAtuais)
                    }
            } catch (_: Exception) {
                _state.value =
                    LancamentosUiState.Erro(
                        "Não foi possível carregar os lançamentos.",
                    )
            }
        }
    }

    fun salvarLancamento(
        usuarioId: String,
        transacao: Transacao,
    ) {
        viewModelScope.launch {
            try {
                val categorias = repository.categorias()
                val dto = transacao.toInsertDto(usuarioId, categorias)
                val salvo = repository.inserir(dto).toTransacao(categorias)

                transacoesAtuais = listOf(salvo) + transacoesAtuais
                _state.value = LancamentosUiState.Sucesso(transacoesAtuais)
            } catch (_: Exception) {
                if (transacoesAtuais.isNotEmpty()) {
                    _state.value = LancamentosUiState.Sucesso(transacoesAtuais)
                }
            }
        }
    }
}
