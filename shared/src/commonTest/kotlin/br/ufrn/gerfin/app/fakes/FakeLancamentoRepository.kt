package br.ufrn.gerfin.app.fakes

import br.ufrn.gerfin.app.domain.model.Categoria
import br.ufrn.gerfin.app.domain.model.Transacao
import br.ufrn.gerfin.app.domain.repository.LancamentoRepository

class FakeLancamentoRepository(
    private val transacoes: List<Transacao> = emptyList(),
    private val categoriasLista: List<Categoria> = emptyList(),
    private val excecao: Exception? = null,
    private val excecaoInserir: Exception? = null,
    respostas: List<Result<List<Transacao>>> = emptyList(),
) : LancamentoRepository {
    private val respostasPendentes =
        respostas.toMutableList().ifEmpty {
            mutableListOf(Result.success(transacoes))
        }

    override suspend fun listar(usuarioId: String): List<Transacao> {
        excecao?.let { throw it }
        val resposta =
            if (respostasPendentes.size > 1) {
                respostasPendentes.removeAt(0)
            } else {
                respostasPendentes.first()
            }
        return resposta.getOrThrow()
    }

    override suspend fun inserir(
        usuarioId: String,
        transacao: Transacao,
    ): Transacao {
        excecaoInserir?.let { throw it }
        excecao?.let { throw it }
        return transacao
    }

    override suspend fun categorias(): List<Categoria> {
        excecao?.let { throw it }
        return categoriasLista
    }
}
