


package br.ufrn.gerfin.app.data

interface LancamentoRepositoryContract {
    suspend fun listar(usuarioId: String): List<LancamentoDto>

    suspend fun inserir(dto: LancamentoInsertDto): LancamentoDto

    suspend fun categorias(): List<CategoriaDto>
}
