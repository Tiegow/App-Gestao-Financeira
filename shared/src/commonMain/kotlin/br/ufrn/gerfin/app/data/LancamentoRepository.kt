package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.model.TipoCategoria
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class LancamentoRepository(
    private val client: SupabaseClient = supabase,
) {
    suspend fun listar(usuarioId: String): List<LancamentoDto> =
        client
            .from("Lancamentos")
            .select {
                filter {
                    eq("faturavel", true)
                    eq("pessoa_id", usuarioId)
                }
                order("data_competencia", Order.DESCENDING)
            }.decodeList()

    suspend fun inserir(dto: LancamentoInsertDto): LancamentoDto =
        client
            .from("Lancamentos")
            .insert(dto) { select() }
            .decodeSingle()

    suspend fun categorias(tipo: TipoCategoria? = null): List<CategoriaDto> =
        client
            .from("categorias")
            .select {
                if (tipo != null) {
                    filter { eq("tipo", tipo.name.lowercase()) }
                }
                order("nome", Order.ASCENDING)
            }.decodeList()
}
