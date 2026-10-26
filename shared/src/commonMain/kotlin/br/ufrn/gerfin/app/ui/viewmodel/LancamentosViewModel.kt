package br.ufrn.gerfin.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.ufrn.gerfin.app.data.LancamentoRepository
import br.ufrn.gerfin.app.data.toInsertDto
import br.ufrn.gerfin.app.data.toTransacao
import br.ufrn.gerfin.app.model.Transacao

class LancamentosViewModel : ViewModel() {
    val repository = LancamentoRepository()
    var transacoes by mutableStateOf<List<Transacao>>(emptyList())
        private set

    suspend fun listarLancamentos(usuarioId: String){
        val categorias = repository.categorias()
        val lancamentos = repository.listar(usuarioId)
        transacoes = lancamentos.map { it.toTransacao(categorias) }
    }

    suspend fun salvarLancamento(
        usuarioId: String,
        transacao: Transacao,
    ) {
        val categorias = repository.categorias()
        val dto = transacao.toInsertDto(usuarioId, categorias)
        val salvo = repository.inserir(dto)

        transacoes = listOf(salvo.toTransacao(categorias)) + transacoes
    }
}
