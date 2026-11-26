package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.domain.model.Categoria
import br.ufrn.gerfin.app.domain.model.Transacao
import br.ufrn.gerfin.app.domain.repository.LancamentoRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class SupabaseLancamentoRepository(
    private val client: SupabaseClient = supabase,
) : LancamentoRepository {
    private suspend fun buscarCategoriasDto(): List<CategoriaDto> =
        client
            .from("categorias")
            .select {
                order("nome", Order.ASCENDING)
            }.decodeList()

    override suspend fun listar(usuarioId: String): List<Transacao> {
        val categoriasDto = buscarCategoriasDto()
        val lancamentosDto: List<LancamentoDto> =
            client
                .from("Lancamentos")
                .select {
                    filter {
                        eq("faturavel", true)
                        eq("pessoa_id", usuarioId)
                    }
                    order("data_competencia", Order.DESCENDING)
                }.decodeList()

        return lancamentosDto.map { it.toTransacao(categoriasDto) }
    }

    override suspend fun inserir(
        usuarioId: String,
        transacao: Transacao,
    ): Transacao {
        val categoriasDto = buscarCategoriasDto()
        val insertDto = transacao.toInsertDto(usuarioId, categoriasDto)
        val salvoDto: LancamentoDto =
            client
                .from("Lancamentos")
                .insert(insertDto) { select() }
                .decodeSingle()

        return salvoDto.toTransacao(categoriasDto)
    }

    override suspend fun categorias(): List<Categoria> = buscarCategoriasDto().map { it.toDomain() }
}
