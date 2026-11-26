package br.ufrn.gerfin.app.domain.model

data class Categoria(
    val id: Int,
    val nome: String,
    val tipo: TipoCategoria,
)
