package br.ufrn.gerfin.app.fixtures

import br.ufrn.gerfin.app.data.LancamentoDto
import br.ufrn.gerfin.app.model.TipoLancamento
import kotlinx.datetime.Instant

fun lancamentoDtoFake(): LancamentoDto {
    return LancamentoDto(
        id = 1,
        valor = 1000,
        faturavel = true,
        recorrente = false,
        dataCompetencia =
            Instant.parse("2026-10-10T00:00:00Z"),
        pago = true,
        tipo = TipoLancamento.DESPESA_SIMPLES,
        categoriaId = 1,
    )
}