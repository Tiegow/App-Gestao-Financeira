package br.ufrn.gerfin.app.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class LancamentoRepository(
    private val client: SupabaseClient = supabase,
) : LancamentoRepositoryContract {
    override suspend fun listar(usuarioId: String): List<LancamentoDto> =
        client
            .from("Lancamentos")
            .select {
                filter {
                    eq("faturavel", true)
                    eq("pessoa_id", usuarioId)
                }
                order("data_competencia", Order.DESCENDING)
            }.decodeList()

    override suspend fun inserir(dto: LancamentoInsertDto): LancamentoDto =
        client
            .from("Lancamentos")
            .insert(dto) { select() }
            .decodeSingle()

    override suspend fun categorias(): List<CategoriaDto> =
        client
            .from("categorias")
            .select {
                order("nome", Order.ASCENDING)
            }.decodeList()
}
