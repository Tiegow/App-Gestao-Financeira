package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.model.FormaPagamento
import br.ufrn.gerfin.app.model.TipoLancamento
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Linha lida do banco. */
@Serializable
data class LancamentoDto(
    val id: Int,
    val valor: Int, // centavos
    @SerialName("lancamento_id") val lancamentoId: Int? = null,
    val faturavel: Boolean,
    val recorrente: Boolean,
    @SerialName("data_competencia") val dataCompetencia: Instant,
    val pago: Boolean,
    @SerialName("pessoa_id") val pessoaId: String? = null,
    val tipo: TipoLancamento,
    @SerialName("forma_pagamento") val formaPagamento: FormaPagamento? = null,
    @SerialName("categoria_id") val categoriaId: Int,
    val descricao: String? = null,
)

/** Dados enviados ao inserir (sem id e sem pessoa_id). */
@Serializable
data class LancamentoInsertDto(
    val valor: Int,
    @SerialName("lancamento_id") val lancamentoId: Int?,
    val faturavel: Boolean,
    val recorrente: Boolean,
    @SerialName("data_competencia") val dataCompetencia: Instant,
    val pago: Boolean,
    val tipo: TipoLancamento,
    @SerialName("forma_pagamento") val formaPagamento: FormaPagamento?,
    @SerialName("categoria_id") val categoriaId: Int,
    val descricao: String?,
)
