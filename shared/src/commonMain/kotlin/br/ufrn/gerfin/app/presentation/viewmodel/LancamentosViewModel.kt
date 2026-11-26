package br.ufrn.gerfin.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufrn.gerfin.app.domain.model.Categoria
import br.ufrn.gerfin.app.domain.model.Transacao
import br.ufrn.gerfin.app.domain.repository.LancamentoRepository
import br.ufrn.gerfin.app.presentation.state.LancamentosUiState
import br.ufrn.gerfin.app.presentation.state.RelatorioAnualUiState
import br.ufrn.gerfin.app.presentation.state.ResumoMensalAnual
import br.ufrn.gerfin.app.utils.formatarValor
import br.ufrn.gerfin.app.utils.parseValor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

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
private const val PARTES_DATA = 3

private fun extrairMesAno(data: String): Pair<Int, Int>? {
    val parts = data.split("/")
    if (parts.size != PARTES_DATA) return null
    val mes = parts[1].toIntOrNull() ?: return null
    val ano = parts[2].toIntOrNull() ?: return null
    return mes to ano
}

private fun calcularRelatorioAnual(
    transacoes: List<Transacao>,
    ano: Int,
): RelatorioAnualUiState {
    val transacoesDoAno =
        transacoes.filter { transacao ->
            extrairMesAno(transacao.data)?.second == ano
        }

    if (transacoesDoAno.isEmpty()) {
        return RelatorioAnualUiState.Vazio
    }

    val meses =
        (1..DEZEMBRO).map { mes ->
            val doMes =
                transacoesDoAno.filter { transacao ->
                    extrairMesAno(transacao.data)?.first == mes
                }
            val entradasMes = doMes.filter { it.isReceita }.sumOf { parseValor(it.valor) }
            val saidasMes = doMes.filter { !it.isReceita }.sumOf { parseValor(it.valor) }
            val saldoMes = entradasMes - saidasMes

            ResumoMensalAnual(
                mesNumero = mes,
                mesNome = MESES_NOMES[mes],
                entradas = formatarValor(entradasMes),
                saidas = formatarValor(saidasMes),
                saldo = formatarValor(saldoMes),
                isSaldoNegativo = saldoMes < 0.0,
            )
        }

    val totalEntradas = transacoesDoAno.filter { it.isReceita }.sumOf { parseValor(it.valor) }
    val totalSaidas = transacoesDoAno.filter { !it.isReceita }.sumOf { parseValor(it.valor) }
    val saldoAcumulado = totalEntradas - totalSaidas

    return RelatorioAnualUiState.Sucesso(
        totalEntradas = formatarValor(totalEntradas),
        totalSaidas = formatarValor(totalSaidas),
        saldoAcumulado = formatarValor(saldoAcumulado),
        meses = meses,
    )
}

class LancamentosViewModel(
    private val repository: LancamentoRepository,
) : ViewModel() {
    private val _state =
        MutableStateFlow<LancamentosUiState>(
            LancamentosUiState.Carregando,
        )

    val state: StateFlow<LancamentosUiState> = _state.asStateFlow()

    private val _relatorioAnualState =
        MutableStateFlow<RelatorioAnualUiState>(
            RelatorioAnualUiState.Carregando,
        )

    val relatorioAnualState: StateFlow<RelatorioAnualUiState> = _relatorioAnualState.asStateFlow()

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private var mesAtual = MES_INICIAL
    private var anoAtual = ANO_INICIAL

    private val _mesAtualNome = MutableStateFlow("${MESES_NOMES[mesAtual]} $anoAtual")
    val mesAtualNome: StateFlow<String> = _mesAtualNome.asStateFlow()

    private val _anoRelatorio = MutableStateFlow(ANO_INICIAL)
    val anoRelatorio: StateFlow<Int> = _anoRelatorio.asStateFlow()

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

    fun anoRelatorioAnterior() {
        _anoRelatorio.value--
        atualizarEstadoAnual()
    }

    fun proximoAnoRelatorio() {
        _anoRelatorio.value++
        atualizarEstadoAnual()
    }

    private fun atualizarEstadoMes() {
        if (_state.value is LancamentosUiState.Carregando || _state.value is LancamentosUiState.Erro) {
            return
        }
        _state.value = calcularEstadoDoMes()
    }

    private fun atualizarEstadoAnual() {
        if (
            _relatorioAnualState.value is RelatorioAnualUiState.Carregando ||
            _relatorioAnualState.value is RelatorioAnualUiState.Erro
        ) {
            return
        }
        _relatorioAnualState.value = calcularRelatorioAnual(transacoesAtuais, _anoRelatorio.value)
    }

    private fun calcularEstadoDoMes(): LancamentosUiState {
        val transacoesDoMes =
            transacoesAtuais.filter { transacao ->
                extrairMesAno(transacao.data) == (mesAtual to anoAtual)
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

    fun carregarCategorias() {
        viewModelScope.launch {
            try {
                _categorias.value = repository.categorias()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                // Mantém lista atual caso falhe em segundo plano
            }
        }
    }

    fun listarLancamentos(usuarioId: String) {
        viewModelScope.launch {
            _state.value = LancamentosUiState.Carregando
            _relatorioAnualState.value = RelatorioAnualUiState.Carregando

            try {
                transacoesAtuais = repository.listar(usuarioId)
                _state.value = calcularEstadoDoMes()
                _relatorioAnualState.value = calcularRelatorioAnual(transacoesAtuais, _anoRelatorio.value)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                val mensagemErro = "Não foi possível carregar os lançamentos."
                _state.value = LancamentosUiState.Erro(mensagemErro)
                _relatorioAnualState.value = RelatorioAnualUiState.Erro(mensagemErro)
            }
        }
    }

    fun salvarLancamento(
        usuarioId: String,
        transacao: Transacao,
    ) {
        viewModelScope.launch {
            try {
                val salvo = repository.inserir(usuarioId, transacao)
                transacoesAtuais = listOf(salvo) + transacoesAtuais
                _state.value = calcularEstadoDoMes()
                _relatorioAnualState.value = calcularRelatorioAnual(transacoesAtuais, _anoRelatorio.value)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                if (_state.value !is LancamentosUiState.Carregando) {
                    _state.value = calcularEstadoDoMes()
                    _relatorioAnualState.value = calcularRelatorioAnual(transacoesAtuais, _anoRelatorio.value)
                }
            }
        }
    }
}
