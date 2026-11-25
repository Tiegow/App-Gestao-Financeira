package br.ufrn.gerfin.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufrn.gerfin.app.data.LancamentoRepositoryContract
import br.ufrn.gerfin.app.data.toInsertDto
import br.ufrn.gerfin.app.data.toTransacao
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.ui.state.LancamentosUiState
import br.ufrn.gerfin.app.utils.formatarValor
import br.ufrn.gerfin.app.utils.parseValor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val MESES_NOMES =
    listOf(
        "",
        "Janeiro",
        "Fevereiro",
        "Março",
        "Abril",
        "Maio",
        "Junho",
        "Julho",
        "Agosto",
        "Setembro",
        "Outubro",
        "Novembro",
        "Dezembro",
    )

private const val MES_INICIAL = 10
private const val ANO_INICIAL = 2026
private const val DEZEMBRO = 12

class LancamentosViewModel(
    private val repository: LancamentoRepositoryContract,
) : ViewModel() {
    private val _state =
        MutableStateFlow<LancamentosUiState>(
            LancamentosUiState.Carregando,
        )

    val state: StateFlow<LancamentosUiState> = _state.asStateFlow()

    private var mesAtual = MES_INICIAL
    private var anoAtual = ANO_INICIAL

    private val _mesAtualNome = MutableStateFlow("${MESES_NOMES[mesAtual]} $anoAtual")
    val mesAtualNome: StateFlow<String> = _mesAtualNome.asStateFlow()

    private var transacoesAtuais: List<Transacao> = emptyList()

    fun mesAnterior() {
        if (mesAtual == 1) {
            mesAtual = DEZEMBRO
            anoAtual--
        } else {
            mesAtual--
        }
        _mesAtualNome.value = "${MESES_NOMES[mesAtual]} $anoAtual"
        atualizarEstadoMes()
    }

    fun proximoMes() {
        if (mesAtual == DEZEMBRO) {
            mesAtual = 1
            anoAtual++
        } else {
            mesAtual++
        }
        _mesAtualNome.value = "${MESES_NOMES[mesAtual]} $anoAtual"
        atualizarEstadoMes()
    }

    private fun atualizarEstadoMes() {
        if (_state.value is LancamentosUiState.Carregando || _state.value is LancamentosUiState.Erro) {
            return
        }
        _state.value = calcularEstadoDoMes()
    }

    private fun calcularEstadoDoMes(): LancamentosUiState {
        val transacoesDoMes =
            transacoesAtuais.filter { transacao ->
                val parts = transacao.data.split("/")
                if (parts.size == 3) {
                    val m = parts[1].toIntOrNull()
                    val y = parts[2].toIntOrNull()
                    m == mesAtual && y == anoAtual
                } else {
                    false
                }
            }

        if (transacoesDoMes.isEmpty()) {
            return LancamentosUiState.Vazio
        }

        val totalReceitas = transacoesDoMes.filter { it.isReceita }.sumOf { parseValor(it.valor) }
        val totalDespesasPagas = transacoesDoMes.filter { !it.isReceita && it.isPago }.sumOf { parseValor(it.valor) }
        val totalDespesasAberto = transacoesDoMes.filter { !it.isReceita && !it.isPago }.sumOf { parseValor(it.valor) }
        val saldoMes = totalReceitas - totalDespesasPagas - totalDespesasAberto

        return LancamentosUiState.Sucesso(
            transacoes = transacoesDoMes,
            saldoEstimado = formatarValor(saldoMes),
            receitas = formatarValor(totalReceitas),
            despesasPagas = formatarValor(totalDespesasPagas),
            despesasAberto = formatarValor(totalDespesasAberto),
        )
    }

    fun listarLancamentos(usuarioId: String) {
        viewModelScope.launch {
            _state.value = LancamentosUiState.Carregando

            try {
                val categorias = repository.categorias()
                val lancamentos = repository.listar(usuarioId)

                transacoesAtuais = lancamentos.map { it.toTransacao(categorias) }
                _state.value = calcularEstadoDoMes()
            } catch (_: Exception) {
                _state.value =
                    LancamentosUiState.Erro(
                        "Não foi possível carregar os lançamentos.",
                    )
            }
        }
    }

    fun salvarLancamento(
        usuarioId: String,
        transacao: Transacao,
    ) {
        viewModelScope.launch {
            try {
                val categorias = repository.categorias()
                val dto = transacao.toInsertDto(usuarioId, categorias)
                val salvo = repository.inserir(dto).toTransacao(categorias)

                transacoesAtuais = listOf(salvo) + transacoesAtuais
                _state.value = calcularEstadoDoMes()
            } catch (_: Exception) {
                if (_state.value !is LancamentosUiState.Carregando) {
                    _state.value = calcularEstadoDoMes()
                }
            }
        }
    }
}
