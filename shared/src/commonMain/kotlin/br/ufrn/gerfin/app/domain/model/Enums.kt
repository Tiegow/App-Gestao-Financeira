package br.ufrn.gerfin.app.domain.model

enum class TipoLancamento {
    RECEITA_SIMPLES,
    DESPESA_SIMPLES,
}

enum class TipoCategoria {
    RECEITA,
    DESPESA,
}

enum class FormaPagamento(
    val label: String,
) {
    PIX("Pix"),
    DINHEIRO("Dinheiro"),
    BOLETO("Boleto"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    TRANSFERENCIA_BANCARIA("Transferência Bancária"),
}
