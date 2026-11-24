package br.ufrn.gerfin.app.fakes

import br.ufrn.gerfin.app.data.CategoriaDto
import br.ufrn.gerfin.app.data.LancamentoDto
import br.ufrn.gerfin.app.data.LancamentoInsertDto
import br.ufrn.gerfin.app.data.LancamentoRepositoryContract

class FakeLancamentoRepository(
    private val lancamentos: List<LancamentoDto> = emptyList(),
    private val excecao: Exception? = null,
    respostas: List<Result<List<LancamentoDto>>> = emptyList(),
) : LancamentoRepositoryContract {
    private val respostasPendentes =
        respostas.toMutableList().ifEmpty {
            mutableListOf(Result.success(lancamentos))
        }

    override suspend fun listar(usuarioId: String): List<LancamentoDto> {
        excecao?.let { throw it }
        val resposta =
            if (respostasPendentes.size > 1) {
                respostasPendentes.removeAt(0)
            } else {
                respostasPendentes.first()
            }
        return resposta.getOrThrow()
    }

    override suspend fun inserir(dto: LancamentoInsertDto): LancamentoDto {
        excecao?.let { throw it }
        return lancamentos.firstOrNull()
            ?: error("Nenhum lançamento configurado para inserção")
    }

    override suspend fun categorias(): List<CategoriaDto> {
        excecao?.let { throw it }
        return emptyList()
    }
}
