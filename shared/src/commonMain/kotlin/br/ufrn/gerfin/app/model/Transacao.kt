package br.ufrn.gerfin.app.model

data class Transacao(
    val id: String,
    val descricao: String,
    val valor: String,
    val isReceita: Boolean,
)
