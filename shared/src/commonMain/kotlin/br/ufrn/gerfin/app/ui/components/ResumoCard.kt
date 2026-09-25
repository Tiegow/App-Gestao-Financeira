package br.ufrn.gerfin.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun ResumoCard(
    saldoEstimado: String,
    receitas: String,
    despesasPagas: String,
    despesasAberto: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "Saldo Estimado do Mês",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "R$ $saldoEstimado",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = "Cálculo baseado nos seus registros na plataforma",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text("Receitas", style = MaterialTheme.typography.labelMedium)
                    Text(
                        "R$ $receitas",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color =
                            androidx.compose.ui.graphics
                                .Color(0xFF2E7D32),
                    )
                }
                Column {
                    Text("Pagas", style = MaterialTheme.typography.labelMedium)
                    Text(
                        "R$ $despesasPagas",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Column {
                    Text("Em Aberto", style = MaterialTheme.typography.labelMedium)
                    Text(
                        "R$ $despesasAberto",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color =
                            androidx.compose.ui.graphics
                                .Color(0xFFC62828),
                    )
                }
            }
        }
    }
}
