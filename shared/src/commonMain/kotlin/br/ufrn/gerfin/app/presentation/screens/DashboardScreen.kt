package br.ufrn.gerfin.app.presentation.screens

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.ufrn.gerfin.app.presentation.components.ResumoCard
import br.ufrn.gerfin.app.presentation.components.TransacaoItem
import br.ufrn.gerfin.app.presentation.state.LancamentosUiState

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun DashboardScreen(
    uiState: LancamentosUiState,
    mesAtualNome: String,
    onMesAnterior: () -> Unit,
    onProximoMes: () -> Unit,
    onTentarNovamente: () -> Unit,
    onNovaTransacao: () -> Unit,
    onAbrirRelatorioAnual: () -> Unit = {},
) {
    val sucesso = uiState as? LancamentosUiState.Sucesso

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovaTransacao,
                modifier =
                    Modifier.semantics {
                        contentDescription = "Adicionar nova transação"
                    },
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        },
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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "GestFin",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(onClick = onAbrirRelatorioAnual) {
                    Text("Relatório Anual")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onMesAnterior) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Mês anterior",
                    )
                }
                Text(
                    text = mesAtualNome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = onProximoMes) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Próximo mês",
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ResumoCard(
                saldoEstimado = sucesso?.saldoEstimado ?: "0,00",
                receitas = sucesso?.receitas ?: "0,00",
                despesasPagas = sucesso?.despesasPagas ?: "0,00",
                despesasAberto = sucesso?.despesasAberto ?: "0,00",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Lançamentos do Mês",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            when (val estado = uiState) {
                is LancamentosUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier =
                                    Modifier.semantics {
                                        contentDescription = "Carregando lançamentos"
                                    },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Carregando lançamentos...")
                        }
                    }
                }

                is LancamentosUiState.Vazio -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Nenhum lançamento encontrado.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onNovaTransacao) {
                                Text("Adicionar lançamento")
                            }
                        }
                    }
                }

                is LancamentosUiState.Sucesso -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(estado.transacoes, key = { it.id }) { transacao ->
                            TransacaoItem(transacao = transacao)
                        }
                    }
                }

                is LancamentosUiState.Erro -> {
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
