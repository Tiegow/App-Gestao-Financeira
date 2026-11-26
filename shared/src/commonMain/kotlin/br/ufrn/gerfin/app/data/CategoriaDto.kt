package br.ufrn.gerfin.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TipoCategoriaDto {
    @SerialName("receita")
    RECEITA,

    @SerialName("despesa")
    DESPESA,
}

@Serializable
data class CategoriaDto(
    val id: Int,
    val nome: String,
    val tipo: TipoCategoriaDto,
)
