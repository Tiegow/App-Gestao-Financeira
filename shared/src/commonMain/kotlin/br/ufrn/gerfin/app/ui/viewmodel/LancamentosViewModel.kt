package br.ufrn.gerfin.app.ui.viewmodel

import br.ufrn.gerfin.app.data.LancamentoDto
import br.ufrn.gerfin.app.data.LancamentoRepository
import br.ufrn.gerfin.app.data.toTransacao
import br.ufrn.gerfin.app.model.Transacao

class LancamentosViewModel   {
    val repository = LancamentoRepository()

    suspend fun listarLancamentos(usuarioId: String): List<Transacao>{
        val categorias = repository.categorias()
        val lancamentos = repository.listar(usuarioId)
        val transacoes = lancamentos.map { it.toTransacao(categorias) }

        return transacoes
    }
}
