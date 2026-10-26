package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.model.FormaPagamento
import br.ufrn.gerfin.app.model.TipoCategoria
import br.ufrn.gerfin.app.model.TipoLancamento
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.utils.formatarValor
import br.ufrn.gerfin.app.utils.parseValor
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.math.round

fun Transacao.toInsertDto(
    usuarioId: String,
    categorias: List<CategoriaDto>,
): LancamentoInsertDto {
    val tipoCategoria = if (isReceita) TipoCategoria.RECEITA else TipoCategoria.DESPESA

    val categoriaId =
        categorias.firstOrNull { it.nome == categoria && it.tipo == tipoCategoria }?.id
            ?: categorias.firstOrNull { it.nome == "Outros" && it.tipo == tipoCategoria }?.id
            ?: error("Categoria não encontrada: $categoria")

    val (dia, mes, ano) = data.split("/").map { it.toInt() }

    return LancamentoInsertDto(
        valor = round(parseValor(valor) * 100).toInt(),
        lancamentoId = null,
        faturavel = true,
        recorrente = false,
        pessoaId = usuarioId,
        dataCompetencia = LocalDate(ano, mes, dia).atStartOfDayIn(TimeZone.UTC),
        pago = isPago,
        tipo = if (isReceita) TipoLancamento.RECEITA_SIMPLES else TipoLancamento.DESPESA_SIMPLES,
        formaPagamento = FormaPagamento.entries.firstOrNull { it.label == formaPagamento },
        categoriaId = categoriaId,
        descricao = observacoes.ifBlank { null },
    )
}

fun LancamentoDto.toTransacao(categorias: List<CategoriaDto>): Transacao {
    val dia = dataCompetencia.toLocalDateTime(TimeZone.UTC).date
    val dataFormatada =
        "${dia.dayOfMonth.toString().padStart(2, '0')}/" +
            "${dia.monthNumber.toString().padStart(2, '0')}/${dia.year}"

    return Transacao(
        id = id.toString(),
        valor = formatarValor(valor / 100.0),
        isReceita = tipo == TipoLancamento.RECEITA_SIMPLES,
        data = dataFormatada,
        categoria = categorias.firstOrNull { it.id == categoriaId }?.nome ?: "Outros",
        formaPagamento = formaPagamento?.label ?: "",
        observacoes = descricao ?: "",
        isPago = pago,
    )
}
