package br.ufrn.gerfin.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.ufrn.gerfin.app.ui.state.RelatorioAnualUiState
import br.ufrn.gerfin.app.ui.state.ResumoMensalAnual
import br.ufrn.gerfin.app.ui.theme.AppTheme
import br.ufrn.gerfin.app.ui.theme.GestFinTheme

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming", "LongMethod")
fun RelatorioAnualScreen(
    uiState: RelatorioAnualUiState,
    anoSelecionado: Int,
    onAnoAnterior: () -> Unit,
    onProximoAno: () -> Unit,
    onTentarNovamente: () -> Unit,
    onVoltar: () -> Unit,
) {
    val sucesso = uiState as? RelatorioAnualUiState.Sucesso

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding(),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                    )
                }
                Text(
                    text = "Relatório Anual",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics { heading() },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onAnoAnterior) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Ano anterior",
                    )
                }
                Text(
                    text = anoSelecionado.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = onProximoAno) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Próximo ano",
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ResumoAnualCard(
                saldoAcumulado = sucesso?.saldoAcumulado ?: "0,00",
                totalEntradas = sucesso?.totalEntradas ?: "0,00",
                totalSaidas = sucesso?.totalSaidas ?: "0,00",
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Consolidado dos 12 Meses",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier =
                    Modifier
                        .padding(bottom = 8.dp)
                        .semantics { heading() },
            )

            when (val estado = uiState) {
                is RelatorioAnualUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier =
                                    Modifier.semantics {
                                        contentDescription = "Carregando relatório anual"
                                    },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Carregando relatório anual...")
                        }
                    }
                }

                is RelatorioAnualUiState.Vazio -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Nenhum lançamento encontrado em $anoSelecionado.")
                    }
                }

                is RelatorioAnualUiState.Sucesso -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(estado.meses, key = { it.mesNumero }) { resumoMes ->
                            MesResumoItem(resumo = resumoMes)
                        }
                    }
                }

                is RelatorioAnualUiState.Erro -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(estado.mensagem)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onTentarNovamente) {
                                Text("Tentar de novo")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
private fun ResumoAnualCard(
    saldoAcumulado: String,
    totalEntradas: String,
    totalSaidas: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Saldo Acumulado no Ano",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = "R$ $saldoAcumulado",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.semantics(mergeDescendants = true) {}) {
                    Text("Entradas", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "R$ $totalEntradas",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.receita,
                    )
                }
                Column(modifier = Modifier.semantics(mergeDescendants = true) {}) {
                    Text("Saídas", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "R$ $totalSaidas",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.despesa,
                    )
                }
            }
        }
    }
}

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
private fun MesResumoItem(resumo: ResumoMensalAnual) {
    val corSaldo = if (resumo.isSaldoNegativo) AppTheme.colors.despesa else AppTheme.colors.receita

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clearAndSetSemantics {
                    contentDescription =
                        "${resumo.mesNome}: entradas R$ ${resumo.entradas}, " +
                        "saídas R$ ${resumo.saidas}, saldo R$ ${resumo.saldo}"
                },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = resumo.mesNome,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Saldo: R$ ${resumo.saldo}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = corSaldo,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+ R$ ${resumo.entradas}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.receita,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "- R$ ${resumo.saidas}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.despesa,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun RelatorioAnualSucessoPreview() {
    GestFinTheme(darkTheme = false) {
        RelatorioAnualScreen(
            uiState =
                RelatorioAnualUiState.Sucesso(
                    totalEntradas = "3.000,00",
                    totalSaidas = "1.200,00",
                    saldoAcumulado = "1.800,00",
                    meses =
                        listOf(
                            ResumoMensalAnual(1, "Janeiro", "3.000,00", "1.200,00", "1.800,00", false),
                            ResumoMensalAnual(2, "Fevereiro", "0,00", "0,00", "0,00", false),
                        ),
                ),
            anoSelecionado = 2026,
            onAnoAnterior = {},
            onProximoAno = {},
            onTentarNovamente = {},
            onVoltar = {},
        )
    }
}

@Preview
@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun RelatorioAnualVazioPreview() {
    GestFinTheme(darkTheme = true) {
        RelatorioAnualScreen(
            uiState = RelatorioAnualUiState.Vazio,
            anoSelecionado = 2025,
            onAnoAnterior = {},
            onProximoAno = {},
            onTentarNovamente = {},
            onVoltar = {},
        )
    }
}
