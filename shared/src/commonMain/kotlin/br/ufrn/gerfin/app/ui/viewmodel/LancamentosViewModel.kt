package br.ufrn.gerfin.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufrn.gerfin.app.data.LancamentoRepository
import br.ufrn.gerfin.app.data.toInsertDto
import br.ufrn.gerfin.app.data.toTransacao
import br.ufrn.gerfin.app.model.Transacao
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class LancamentosViewModel : ViewModel() {
    val repository = LancamentoRepository()
    var transacoes by mutableStateOf<List<Transacao>>(emptyList())
        private set
    var carregando by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf<String?>(null)
        private set

    fun listarLancamentos(usuarioId: String) {
        viewModelScope.launch {
            carregando = true
            mensagemErro = null

            try {
                val categorias = repository.categorias()
                val lancamentos = repository.listar(usuarioId)

                transacoes = lancamentos.map { it.toTransacao(categorias) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensagemErro = "Não foi possível carregar os lançamentos."
            } finally {
                carregando = false
            }
        }
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
