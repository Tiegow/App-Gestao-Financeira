package br.ufrn.gerfin.app.model

data class Transacao(
    val id: String,
    val valor: String,
    val isReceita: Boolean,
    val data: String,
    val categoria: String,
    val formaPagamento: String,
    val observacoes: String,
)
