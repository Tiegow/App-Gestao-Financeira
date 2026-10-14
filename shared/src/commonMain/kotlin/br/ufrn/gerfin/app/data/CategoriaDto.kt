package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.model.TipoCategoria
import kotlinx.serialization.Serializable

@Serializable
data class CategoriaDto(
    val id: Int,
    val nome: String,
    val tipo: TipoCategoria,
)
