package br.ufrn.gerfin.app.ui.state

import br.ufrn.gerfin.app.model.Transacao

sealed interface LancamentosUiState {
    data object Carregando : LancamentosUiState

    data object Vazio : LancamentosUiState

    data class Sucesso(
        val transacoes: List<Transacao>,
    ) : LancamentosUiState

    data class Erro(
        val mensagem: String,
    ) : LancamentosUiState
}