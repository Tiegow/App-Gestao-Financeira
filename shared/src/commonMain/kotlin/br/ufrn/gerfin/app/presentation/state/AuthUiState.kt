package br.ufrn.gerfin.app.presentation.state

data class AuthUiState(
    val isCarregando: Boolean = false,
    val mensagemErro: String? = null,
    val loginConcluido: Boolean = false,
    val cadastroConcluido: Boolean = false,
)
