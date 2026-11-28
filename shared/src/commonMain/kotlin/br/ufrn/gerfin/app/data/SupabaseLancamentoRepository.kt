package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.domain.model.Categoria
import br.ufrn.gerfin.app.domain.model.Transacao
import br.ufrn.gerfin.app.domain.repository.LancamentoRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CancellationException

class SupabaseLancamentoRepository(
    private val client: SupabaseClient = supabase,
) : LancamentoRepository {
    private suspend fun buscarCategoriasDto(): List<CategoriaDto> =
        client
            .from("categorias")
            .select {
                order("nome", Order.ASCENDING)
            }.decodeList()

    override suspend fun listar(usuarioId: String): List<Transacao> =
        withNetworkFailureMapping {
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

            lancamentosDto.map { it.toTransacao(categoriasDto) }
        }

    override suspend fun inserir(
        usuarioId: String,
        transacao: Transacao,
    ): Transacao =
        withNetworkFailureMapping {
            val categoriasDto = buscarCategoriasDto()
            val insertDto = transacao.toInsertDto(usuarioId, categoriasDto)
            val salvoDto: LancamentoDto =
                client
                    .from("Lancamentos")
                    .insert(insertDto) { select() }
                    .decodeSingle()

            salvoDto.toTransacao(categoriasDto)
        }

    override suspend fun categorias(): List<Categoria> =
        withNetworkFailureMapping {
            buscarCategoriasDto().map { it.toDomain() }
        }

    private suspend fun <T> withNetworkFailureMapping(block: suspend () -> T): T =
        try {
            block()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            throw exception.toLancamentoFailure()
        }
}
