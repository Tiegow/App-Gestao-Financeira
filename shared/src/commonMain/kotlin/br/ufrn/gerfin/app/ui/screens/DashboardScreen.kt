package br.ufrn.gerfin.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.ui.components.ResumoCard
import br.ufrn.gerfin.app.ui.components.TransacaoItem

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun DashboardScreen(
    mesAtualNome: String,
    onMesAnterior: () -> Unit,
    onProximoMes: () -> Unit,
    saldoEstimado: String,
    receitas: String,
    despesasPagas: String,
    despesasAberto: String,
    transacoes: List<Transacao>,
    onNovaTransacao: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNovaTransacao) {
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
            Text(
                text = "GestFin",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )

            // Seletor de Mês
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onMesAnterior) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Mês anterior")
                }
                Text(
                    text = mesAtualNome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = onProximoMes) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Próximo mês")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ResumoCard(
                saldoEstimado = saldoEstimado,
                receitas = receitas,
                despesasPagas = despesasPagas,
                despesasAberto = despesasAberto,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Lançamentos do Mês",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(transacoes, key = { it.id }) { transacao ->
                    TransacaoItem(transacao = transacao)
                }
            }
        }
    }
}
