package br.ufrn.gerfin.app.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TipoLancamentoDto {
    @SerialName("receita_simples")
    RECEITA_SIMPLES,

    @SerialName("despesa_simples")
    DESPESA_SIMPLES,
}

@Serializable
enum class FormaPagamentoDto(
    val label: String,
) {
    @SerialName("pix")
    PIX("Pix"),

    @SerialName("dinheiro")
    DINHEIRO("Dinheiro"),

    @SerialName("boleto")
    BOLETO("Boleto"),

    @SerialName("cartao_credito")
    CARTAO_CREDITO("Cartão de Crédito"),

    @SerialName("cartao_debito")
    CARTAO_DEBITO("Cartão de Débito"),

    @SerialName("transferencia_bancaria")
    TRANSFERENCIA_BANCARIA("Transferência Bancária"),
}

@Serializable
data class LancamentoDto(
    val id: Int,
    val valor: Int,
    @SerialName("lancamento_id") val lancamentoId: Int? = null,
    val faturavel: Boolean,
    val recorrente: Boolean,
    @SerialName("data_competencia") val dataCompetencia: Instant,
    val pago: Boolean,
    @SerialName("pessoa_id") val pessoaId: String? = null,
    val tipo: TipoLancamentoDto,
    @SerialName("forma_pagamento") val formaPagamento: FormaPagamentoDto? = null,
    @SerialName("categoria_id") val categoriaId: Int,
    val descricao: String? = null,
)

@Serializable
data class LancamentoInsertDto(
    val valor: Int,
    @SerialName("lancamento_id") val lancamentoId: Int?,
    val faturavel: Boolean,
    val recorrente: Boolean,
    @SerialName("data_competencia") val dataCompetencia: Instant,
    val pago: Boolean,
    @SerialName("pessoa_id") val pessoaId: String,
    val tipo: TipoLancamentoDto,
    @SerialName("forma_pagamento") val formaPagamento: FormaPagamentoDto?,
    @SerialName("categoria_id") val categoriaId: Int,
    val descricao: String?,
)
