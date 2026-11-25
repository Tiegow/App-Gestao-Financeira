package br.ufrn.gerfin.app.ui.state

data class ResumoMensalAnual(
    val mesNumero: Int,
    val mesNome: String,
    val entradas: String,
    val saidas: String,
    val saldo: String,
    val isSaldoNegativo: Boolean,
)

sealed interface RelatorioAnualUiState {
    data object Carregando : RelatorioAnualUiState

    data object Vazio : RelatorioAnualUiState

    data class Sucesso(
        val totalEntradas: String,
        val totalSaidas: String,
        val saldoAcumulado: String,
        val meses: List<ResumoMensalAnual>,
    ) : RelatorioAnualUiState

    data class Erro(
        val mensagem: String,
    ) : RelatorioAnualUiState
}
