package br.ufrn.gerfin.app.presentation.state

import br.ufrn.gerfin.app.domain.model.Transacao

enum class TipoFalhaLancamentos {
    CONEXAO,
    RESPOSTA_INVALIDA,
    INESPERADA,
}

sealed interface LancamentosUiState {
    data object Carregando : LancamentosUiState

    data object Vazio : LancamentosUiState

    data class Sucesso(
        val transacoes: List<Transacao>,
        val saldoEstimado: String = "0,00",
        val receitas: String = "0,00",
        val despesasPagas: String = "0,00",
        val despesasAberto: String = "0,00",
    ) : LancamentosUiState

    data class Erro(
        val mensagem: String,
        val tipo: TipoFalhaLancamentos = TipoFalhaLancamentos.INESPERADA,
    ) : LancamentosUiState
}
