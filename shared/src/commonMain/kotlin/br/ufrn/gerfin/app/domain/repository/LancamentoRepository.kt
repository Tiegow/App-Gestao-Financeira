package br.ufrn.gerfin.app.domain.repository

import br.ufrn.gerfin.app.domain.model.Categoria
import br.ufrn.gerfin.app.domain.model.Transacao

interface LancamentoRepository {
    suspend fun listar(usuarioId: String): List<Transacao>

    suspend fun inserir(
        usuarioId: String,
        transacao: Transacao,
    ): Transacao

    suspend fun categorias(): List<Categoria>
}
