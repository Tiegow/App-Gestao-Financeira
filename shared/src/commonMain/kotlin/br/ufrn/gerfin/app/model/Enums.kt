package br.ufrn.gerfin.app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TipoLancamento {
    @SerialName("receita_simples")
    RECEITA_SIMPLES,

    @SerialName("despesa_simples")
    DESPESA_SIMPLES,
}

@Serializable
enum class TipoCategoria {
    @SerialName("receita")
    RECEITA,

    @SerialName("despesa")
    DESPESA,
}

@Serializable
enum class FormaPagamento(
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
