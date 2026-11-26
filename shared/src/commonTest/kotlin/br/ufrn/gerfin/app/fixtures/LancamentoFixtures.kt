package br.ufrn.gerfin.app.fixtures

import br.ufrn.gerfin.app.data.LancamentoDto
import br.ufrn.gerfin.app.data.TipoLancamentoDto
import br.ufrn.gerfin.app.domain.model.Transacao
import kotlinx.datetime.Instant

fun transacaoFake(
    id: String = "1",
    valor: String = "10,00",
    isReceita: Boolean = false,
    data: String = "10/10/2026",
    categoria: String = "Alimentação",
    formaPagamento: String = "Pix",
    observacoes: String = "",
    isPago: Boolean = true,
): Transacao =
    Transacao(
        id = id,
        valor = valor,
        isReceita = isReceita,
        data = data,
        categoria = categoria,
        formaPagamento = formaPagamento,
        observacoes = observacoes,
        isPago = isPago,
    )

fun lancamentoDtoFake(): LancamentoDto =
    LancamentoDto(
        id = 1,
        valor = 1000,
        faturavel = true,
        recorrente = false,
        dataCompetencia = Instant.parse("2026-10-10T00:00:00Z"),
        pago = true,
        tipo = TipoLancamentoDto.DESPESA_SIMPLES,
        categoriaId = 1,
    )
